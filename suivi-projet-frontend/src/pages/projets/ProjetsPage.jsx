import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { projetService, organismeService, employeService } from '../../services/services';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Plus, Search, Pencil, Trash2, FolderKanban, Eye, TrendingUp } from 'lucide-react';

function ProjetForm({ initial, organismes, employes, onSave, onClose }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: initial ? {
      ...initial,
      organismeId: initial.organisme?.id,
      chefProjetId: initial.chefProjet?.id,
      dateDebut: initial.dateDebut?.substring(0, 10),
      dateFin: initial.dateFin?.substring(0, 10),
    } : {}
  });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        <div className="form-group">
          <label className="form-label">Code</label>
          <input className="form-control" placeholder="PRJ-2026-001"
            {...register('code', { required: 'Requis' })} />
          {errors.code && <span className="form-error">{errors.code.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Nom</label>
          <input className="form-control" placeholder="Nom du projet"
            {...register('nom', { required: 'Requis' })} />
          {errors.nom && <span className="form-error">{errors.nom.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Date début</label>
          <input className="form-control" type="date"
            {...register('dateDebut', { required: 'Requis' })} />
          {errors.dateDebut && <span className="form-error">{errors.dateDebut.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Date fin</label>
          <input className="form-control" type="date"
            {...register('dateFin', { required: 'Requis' })} />
          {errors.dateFin && <span className="form-error">{errors.dateFin.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Montant (MAD)</label>
          <input className="form-control" type="number" step="0.01" placeholder="75000"
            {...register('montant', { required: 'Requis', valueAsNumber: true, min: { value: 0, message: '> 0' } })} />
          {errors.montant && <span className="form-error">{errors.montant.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Organisme</label>
          <select className="form-control" {...register('organismeId', { required: 'Requis', valueAsNumber: true })}>
            <option value="">-- Sélectionner --</option>
            {organismes.map(o => <option key={o.id} value={o.id}>{o.nom}</option>)}
          </select>
          {errors.organismeId && <span className="form-error">{errors.organismeId.message}</span>}
        </div>
        <div className="form-group" style={{ gridColumn: 'span 2' }}>
          <label className="form-label">Chef de projet</label>
          <select className="form-control" {...register('chefProjetId', { required: 'Requis', valueAsNumber: true })}>
            <option value="">-- Sélectionner --</option>
            {employes.map(e => <option key={e.id} value={e.id}>{e.prenom} {e.nom} ({e.profil?.libelle})</option>)}
          </select>
          {errors.chefProjetId && <span className="form-error">{errors.chefProjetId.message}</span>}
        </div>
        <div className="form-group" style={{ gridColumn: 'span 2' }}>
          <label className="form-label">Description</label>
          <textarea className="form-control" rows={3} placeholder="Description du projet..."
            {...register('description')} />
        </div>
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary"><Plus size={14} /> Enregistrer</button>
      </div>
    </form>
  );
}

export default function ProjetsPage() {
  const { hasRole } = useAuth();
  const [items, setItems]         = useState([]);
  const [filtered, setFiltered]   = useState([]);
  const [organismes, setOrganismes] = useState([]);
  const [employes, setEmployes]   = useState([]);
  const [loading, setLoading]     = useState(true);
  const [search, setSearch]       = useState('');
  const [modal, setModal]         = useState(null);
  const [selected, setSelected]   = useState(null);
  const [deleteId, setDeleteId]   = useState(null);

  const load = async () => {
    try {
      const [pRes, oRes, eRes] = await Promise.all([
        projetService.getAll(),
        organismeService.getAll(),
        employeService.getAll(),
      ]);
      setItems(pRes.data);
      setFiltered(pRes.data);
      setOrganismes(oRes.data);
      setEmployes(eRes.data);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    const q = search.toLowerCase();
    setFiltered(items.filter(p =>
      p.nom?.toLowerCase().includes(q) ||
      p.code?.toLowerCase().includes(q) ||
      p.organisme?.nom?.toLowerCase().includes(q)
    ));
  }, [search, items]);

  const handleSave = async (data) => {
    try {
      const payload = { ...data, organismeId: Number(data.organismeId), chefProjetId: Number(data.chefProjetId), montant: Number(data.montant) };
      if (modal === 'edit') {
        await projetService.update(selected.id, payload);
        toast.success('Projet mis à jour');
      } else {
        await projetService.create(payload);
        toast.success('Projet créé');
      }
      setModal(null);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erreur');
    }
  };

  const handleDelete = async () => {
    try {
      await projetService.delete(deleteId);
      toast.success('Projet supprimé');
      load();
    } catch { toast.error('Impossible de supprimer'); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Projets</h1>
          <p className="page-subtitle">{filtered.length} projet(s)</p>
        </div>
        {hasRole('SECRETAIRE','DIRECTEUR','ADMINISTRATEUR') && (
          <button className="btn btn-primary" onClick={() => { setSelected(null); setModal('create'); }}>
            <Plus size={16} /> Nouveau projet
          </button>
        )}
      </div>

      <div className="card mb-6">
        <div className="search-bar" style={{ maxWidth: 400 }}>
          <Search size={15} color="var(--text-muted)" />
          <input placeholder="Nom, code, organisme..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? <div className="loading-center"><div className="spinner" /></div>
        : filtered.length === 0 ? (
          <div className="empty-state"><FolderKanban size={40} /><p>Aucun projet</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead><tr>
                <th>Code</th><th>Nom</th><th>Organisme</th><th>Chef de projet</th><th>Budget</th><th>Période</th><th>Actions</th>
              </tr></thead>
              <tbody>
                {filtered.map(p => (
                  <tr key={p.id}>
                    <td><code style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{p.nom}</div>
                      {p.description?.includes('CLÔTURÉ') && <span className="badge badge-gray" style={{ fontSize: '0.65rem' }}>Clôturé</span>}
                    </td>
                    <td>{p.organisme?.nom}</td>
                    <td>{p.chefProjet?.prenom} {p.chefProjet?.nom}</td>
                    <td style={{ fontWeight: 600, color: 'var(--accent-blue-light)' }}>
                      {p.montant?.toLocaleString('fr-FR')} MAD
                    </td>
                    <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                      {p.dateDebut} → {p.dateFin}
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        <Link to={`/projets/${p.id}`} className="btn btn-secondary btn-icon btn-sm" title="Détail">
                          <Eye size={13} />
                        </Link>
                        {hasRole('DIRECTEUR','ADMINISTRATEUR','SECRETAIRE') && (
                          <>
                            <button className="btn btn-secondary btn-icon btn-sm" onClick={() => { setSelected(p); setModal('edit'); }}>
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-icon btn-sm" onClick={() => setDeleteId(p.id)}>
                              <Trash2 size={13} />
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <Modal open={modal === 'create' || modal === 'edit'} onClose={() => setModal(null)}
        title={modal === 'edit' ? 'Modifier le projet' : 'Nouveau projet'} size="lg">
        <ProjetForm initial={selected} organismes={organismes} employes={employes}
          onSave={handleSave} onClose={() => setModal(null)} />
      </Modal>

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete}
        message="Supprimer ce projet et toutes ses données ?" />
    </div>
  );
}
