import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { projetService, organismeService, employeService } from '../../services/services';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Search, Plus, Eye, Pencil, Trash2, UserPlus, DollarSign } from 'lucide-react';

function ProjetForm({ initial, organismes, employes, onSave, onClose }) {
  const { hasRole } = useAuth();
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
          <label className="form-label">ID Organisme</label>
          <input className="form-control" type="number" placeholder="Ex: 5"
            {...register('organismeId', { required: 'ID Organisme requis', valueAsNumber: true })} />
          {errors.organismeId && <span className="form-error">{errors.organismeId.message}</span>}
        </div>
        <div className="form-group" style={{ gridColumn: 'span 2' }}>
          <label className="form-label">ID Chef de projet</label>
          <input className="form-control" type="number" placeholder="Ex: 12"
            {...register('chefProjetId', { required: 'ID Chef requis', valueAsNumber: true })} />
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
    setLoading(true);
    try {
      // On lance le chargement des projets
      const pRes = await projetService.getAll();
      setItems(pRes.data);
      if (!search) setFiltered(pRes.data);

      // On charge le reste de manière résiliente : si l'un échoue (ex: 403), on continue
      try {
        const oRes = await organismeService.getAll();
        setOrganismes(oRes.data);
      } catch (e) { console.warn("Organismes non chargés (Access Denied ?)", e); }

      try {
        const eRes = await employeService.getAll();
        setEmployes(eRes.data);
      } catch (e) { console.warn("Employés non chargés (Access Denied ?)", e); }

    } catch (err) {
      const msg = typeof err.response?.data === 'string' ? err.response.data : err.response?.data?.message || 'Erreur de chargement';
      toast.error(msg);
    } finally {
      setLoading(false);
    }
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
      setSearch('');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erreur');
    }
  };

  const handleUpdateMontant = async (val) => {
    try {
        await projetService.updateMontant(selected.id, val);
        toast.success('Montant mis à jour');
        setModal(null);
        load();
    } catch (err) {
        const msg = typeof err.response?.data === 'string' ? err.response.data : err.response?.data?.message || 'Erreur';
        toast.error(msg);
    }
  };

  const handleAffectChef = async (val) => {
    try {
        await projetService.affecterChef(selected.id, val);
        toast.success('Chef de projet affecté');
        setModal(null);
        load();
    } catch (err) {
        const msg = typeof err.response?.data === 'string' ? err.response.data : err.response?.data?.message || 'Erreur';
        toast.error(msg);
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

      <div className="card mb-6" style={{ display: 'flex', gap: 16, alignItems: 'center', flexWrap: 'wrap' }}>
        <div className="search-bar" style={{ flex: 1, minWidth: 250 }}>
          <Search size={15} color="var(--text-muted)" />
          <input placeholder="Nom, code, organisme..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>

        {hasRole('DIRECTEUR', 'ADMINISTRATEUR') && (
            <div style={{ display: 'flex', gap: 8 }}>
                <button className="btn btn-secondary btn-sm" onClick={async () => {
                    const code = prompt("Entrez le code du projet :");
                    if (code) {
                        try {
                            const res = await projetService.getByCode(code);
                            setFiltered([res.data]);
                        } catch { toast.error("Projet introuvable"); }
                    }
                }}>Par Code</button>
                <button className="btn btn-secondary btn-sm" onClick={async () => {
                    const m = prompt("Entrez le montant exact :");
                    if (m) {
                        try {
                            const res = await projetService.getByMontant(Number(m));
                            setFiltered(res.data);
                        } catch { toast.error("Aucun projet trouvé"); }
                    }
                }}>Par Montant</button>
                <button className="btn btn-text btn-sm" onClick={load}>Réinitialiser</button>
            </div>
        )}
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? <div className="loading-center"><div className="spinner" /></div>
        : filtered.length === 0 ? (
          <div className="empty-state"><div /> <p>Aucun projet</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead><tr>
                {hasRole('DIRECTEUR', 'ADMINISTRATEUR') && <th>ID</th>}
                <th>Code</th><th>Nom</th><th>Organisme</th>
                <th>Chef de projet</th>
                <th>Budget</th><th>Période</th><th>Actions</th>
              </tr></thead>
              <tbody>
                {filtered.map(p => (
                  <tr key={p.id}>
                    {hasRole('DIRECTEUR', 'ADMINISTRATEUR') && <td><span className="badge badge-gray">{p.id}</span></td>}
                    <td><code style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{p.code}</code></td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{p.nom}</div>
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
                        {hasRole('DIRECTEUR') && (
                          <>
                            <button className="btn btn-secondary btn-icon btn-sm" title="Mettre à jour montant" onClick={() => { setSelected(p); setModal('update-montant'); }}>
                                <DollarSign size={13} />
                            </button>
                            <button className="btn btn-secondary btn-icon btn-sm" title="Affecter chef" onClick={() => { setSelected(p); setModal('affect-chef'); }}>
                                <UserPlus size={13} />
                            </button>
                          </>
                        )}
                        {hasRole('SECRETAIRE','ADMINISTRATEUR') && (
                          <button className="btn btn-secondary btn-icon btn-sm" title="Modifier" onClick={() => { setSelected(p); setModal('edit'); }}>
                            <Pencil size={13} />
                          </button>
                        )}
                        {hasRole('ADMINISTRATEUR') && (
                          <button className="btn btn-danger btn-icon btn-sm" title="Supprimer" onClick={() => setDeleteId(p.id)}>
                            <Trash2 size={13} />
                          </button>
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

      {/* Modal Affectation Chef */}
      <Modal open={modal === 'affect-chef'} onClose={() => setModal(null)} title="Affecter un chef de projet">
        <form onSubmit={(e) => { e.preventDefault(); handleAffectChef(Number(e.target.chefId.value)); }} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="form-group">
            <label className="form-label">ID du chef de projet</label>
            <input name="chefId" type="number" className="form-control" required placeholder="Ex: 5" />
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={() => setModal(null)}>Annuler</button>
            <button type="submit" className="btn btn-primary">Affecter</button>
          </div>
        </form>
      </Modal>

      {/* Modal Montant */}
      <Modal open={modal === 'update-montant'} onClose={() => setModal(null)} title="Mettre à jour le budget">
        <form onSubmit={(e) => { e.preventDefault(); handleUpdateMontant(Number(e.target.montant.value)); }} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="form-group">
            <label className="form-label">Nouveau montant (MAD)</label>
            <input name="montant" type="number" className="form-control" required placeholder="Ex: 50000" defaultValue={selected?.montant} />
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={() => setModal(null)}>Annuler</button>
            <button type="submit" className="btn btn-primary">Mettre à jour</button>
          </div>
        </form>
      </Modal>

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete} message="Supprimer ce projet ?" />
    </div>
  );
}
