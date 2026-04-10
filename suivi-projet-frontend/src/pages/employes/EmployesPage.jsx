import { useState, useEffect } from 'react';
import { employeService, profilService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Plus, Search, Pencil, Trash2, Users, Calendar } from 'lucide-react';

const ROLE_COLORS = {
  'ADMINISTRATEUR': 'badge-rose',
  'DIRECTEUR':      'badge-violet',
  'CHEF_PROJET':    'badge-blue',
  'COMPTABLE':      'badge-amber',
  'SECRETAIRE':     'badge-cyan',
};

function EmployeForm({ initial, profils, onSave, onClose, editMode }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: initial ? {
      ...initial,
      profilId: initial.profil?.id,
      password: '',
    } : {}
  });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        <div className="form-group">
          <label className="form-label">Matricule</label>
          <input className="form-control" placeholder="ABCDEFG"
            {...register('matricule', { required: 'Requis' })} />
          {errors.matricule && <span className="form-error">{errors.matricule.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Login</label>
          <input className="form-control" placeholder="jean.dupont"
            {...register('login', { required: 'Requis' })} />
          {errors.login && <span className="form-error">{errors.login.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Prénom</label>
          <input className="form-control" placeholder="Jean"
            {...register('prenom', { required: 'Requis' })} />
          {errors.prenom && <span className="form-error">{errors.prenom.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Nom</label>
          <input className="form-control" placeholder="DUPONT"
            {...register('nom', { required: 'Requis' })} />
          {errors.nom && <span className="form-error">{errors.nom.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Email</label>
          <input className="form-control" type="email" placeholder="jean@exemple.com"
            {...register('email', { required: 'Requis' })} />
          {errors.email && <span className="form-error">{errors.email.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Téléphone</label>
          <input className="form-control" placeholder="0600000000"
            {...register('telephone')} />
        </div>
        <div className="form-group">
          <label className="form-label">Profil</label>
          <select className="form-control" {...register('profilId', { required: 'Requis', valueAsNumber: true })}>
            <option value="">-- Sélectionner --</option>
            {profils.map(p => <option key={p.id} value={p.id}>{p.libelle}</option>)}
          </select>
          {errors.profilId && <span className="form-error">{errors.profilId.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Mot de passe {editMode && '(laisser vide = inchangé)'}</label>
          <input className="form-control" type="password" placeholder="••••••••"
            {...register('password', editMode ? {} : { required: 'Requis', minLength: { value: 6, message: '6 char. min.' } })} />
          {errors.password && <span className="form-error">{errors.password.message}</span>}
        </div>
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary"><Plus size={14} /> Enregistrer</button>
      </div>
    </form>
  );
}

export default function EmployesPage() {
  const { hasRole } = useAuth();
  const [items, setItems]       = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [profils, setProfils]   = useState([]);
  const [loading, setLoading]   = useState(true);
  const [search, setSearch]     = useState('');
  const [modal, setModal]       = useState(null);
  const [selected, setSelected] = useState(null);
  const [deleteId, setDeleteId] = useState(null);
  const [dispoModal, setDispoModal] = useState(false);
  const [dispoDate, setDispoDate]   = useState({ dateDebut: '', dateFin: '' });
  const [dispoResult, setDispoResult] = useState(null);

  const load = async () => {
    try {
      const [empRes, profilRes] = await Promise.all([
        employeService.getAll(),
        profilService.getAll(),
      ]);
      setItems(empRes.data);
      setFiltered(empRes.data);
      setProfils(profilRes.data);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    const q = search.toLowerCase();
    setFiltered(items.filter(e =>
      e.nom?.toLowerCase().includes(q) ||
      e.prenom?.toLowerCase().includes(q) ||
      e.matricule?.toLowerCase().includes(q) ||
      e.email?.toLowerCase().includes(q)
    ));
  }, [search, items]);

  const handleSave = async (data) => {
    try {
      const payload = { ...data, profilId: Number(data.profilId) };
      if (modal === 'edit') {
        if (!payload.password) delete payload.password;
        await employeService.update(selected.id, payload);
        toast.success('Employé mis à jour');
      } else {
        await employeService.create(payload);
        toast.success('Employé créé');
      }
      setModal(null);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erreur');
    }
  };

  const handleDelete = async () => {
    try {
      await employeService.delete(deleteId);
      toast.success('Employé supprimé');
      load();
    } catch { toast.error('Erreur de suppression'); }
  };

  const checkDispo = async () => {
    if (!dispoDate.dateDebut || !dispoDate.dateFin) return;
    try {
      const res = await employeService.disponibles(dispoDate.dateDebut, dispoDate.dateFin);
      setDispoResult(res.data);
    } catch { toast.error('Erreur'); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Employés</h1>
          <p className="page-subtitle">{filtered.length} employé(s)</p>
        </div>
        <div style={{ display: 'flex', gap: 10 }}>
          <button className="btn btn-secondary" onClick={() => { setDispoResult(null); setDispoModal(true); }}>
            <Calendar size={15} /> Disponibilités
          </button>
          {hasRole('ADMINISTRATEUR') && (
            <button className="btn btn-primary" onClick={() => { setSelected(null); setModal('create'); }}>
              <Plus size={16} /> Nouvel employé
            </button>
          )}
        </div>
      </div>

      <div className="card mb-6">
        <div className="search-bar" style={{ maxWidth: 400 }}>
          <Search size={15} color="var(--text-muted)" />
          <input placeholder="Nom, prénom, matricule, email..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? <div className="loading-center"><div className="spinner" /></div>
        : filtered.length === 0 ? (
          <div className="empty-state"><Users size={40} /><p>Aucun employé trouvé</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead><tr>
                <th>Matricule</th><th>Nom</th><th>Email</th><th>Téléphone</th><th>Profil</th><th>Actions</th>
              </tr></thead>
              <tbody>
                {filtered.map(e => (
                  <tr key={e.id}>
                    <td><code style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{e.matricule}</code></td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{e.prenom} {e.nom}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{e.login}</div>
                    </td>
                    <td>{e.email}</td>
                    <td>{e.telephone}</td>
                    <td>
                      <span className={`badge ${ROLE_COLORS[e.profil?.libelle] || 'badge-gray'}`}>
                        {e.profil?.libelle?.replace(/_/g, ' ')}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        {hasRole('ADMINISTRATEUR') && (
                          <>
                            <button className="btn btn-secondary btn-icon btn-sm" onClick={() => { setSelected(e); setModal('edit'); }}>
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-icon btn-sm" onClick={() => setDeleteId(e.id)}>
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
        title={modal === 'edit' ? 'Modifier l\'employé' : 'Nouvel employé'} size="lg">
        <EmployeForm initial={selected} profils={profils} onSave={handleSave}
          onClose={() => setModal(null)} editMode={modal === 'edit'} />
      </Modal>

      {/* Disponibilité modal */}
      <Modal open={dispoModal} onClose={() => setDispoModal(false)} title="Vérifier les disponibilités">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label">Date début</label>
              <input className="form-control" type="date"
                value={dispoDate.dateDebut} onChange={e => setDispoDate(p => ({ ...p, dateDebut: e.target.value }))} />
            </div>
            <div className="form-group">
              <label className="form-label">Date fin</label>
              <input className="form-control" type="date"
                value={dispoDate.dateFin} onChange={e => setDispoDate(p => ({ ...p, dateFin: e.target.value }))} />
            </div>
          </div>
          <button className="btn btn-primary" onClick={checkDispo}><Calendar size={14} /> Vérifier</button>

          {dispoResult !== null && (
            <div>
              <div style={{ fontWeight: 600, marginBottom: 10, color: 'var(--accent-emerald)' }}>
                {dispoResult.length} employé(s) disponible(s)
              </div>
              {dispoResult.map(e => (
                <div key={e.id} style={{ display: 'flex', alignItems: 'center', gap: 10, padding: '8px 0', borderBottom: '1px solid var(--border)', fontSize: '0.85rem' }}>
                  <div style={{ width: 32, height: 32, borderRadius: '50%', background: 'var(--bg-hover)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.75rem', fontWeight: 700, color: 'var(--accent-blue-light)' }}>
                    {e.prenom?.[0]}{e.nom?.[0]}
                  </div>
                  <div>
                    <div>{e.prenom} {e.nom}</div>
                    <div style={{ color: 'var(--text-muted)', fontSize: '0.75rem' }}>{e.profil?.libelle}</div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </Modal>

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete}
        message="Supprimer cet employé ?" />
    </div>
  );
}
