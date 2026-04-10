import { useState, useEffect } from 'react';
import { profilService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Plus, Pencil, Trash2, Shield } from 'lucide-react';

const ROLE_COLORS = {
  'ADMINISTRATEUR': 'badge-rose',
  'DIRECTEUR':      'badge-violet',
  'CHEF_PROJET':    'badge-blue',
  'COMPTABLE':      'badge-amber',
  'SECRETAIRE':     'badge-cyan',
};

function ProfilForm({ initial, onSave, onClose }) {
  const { register, handleSubmit, formState: { errors } } = useForm({ defaultValues: initial || {} });
  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-group">
        <label className="form-label">Code</label>
        <input className="form-control" placeholder="chef de projet"
          {...register('code', { required: 'Requis' })} />
        {errors.code && <span className="form-error">{errors.code.message}</span>}
      </div>
      <div className="form-group">
        <label className="form-label">Libellé</label>
        <input className="form-control" placeholder="CHEF_PROJET"
          {...register('libelle', { required: 'Requis' })} />
        {errors.libelle && <span className="form-error">{errors.libelle.message}</span>}
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary"><Plus size={14} /> Enregistrer</button>
      </div>
    </form>
  );
}

export default function ProfilsPage() {
  const { hasRole } = useAuth();
  const [items, setItems]       = useState([]);
  const [loading, setLoading]   = useState(true);
  const [modal, setModal]       = useState(null);
  const [selected, setSelected] = useState(null);
  const [deleteId, setDeleteId] = useState(null);

  const load = async () => {
    try {
      const res = await profilService.getAll();
      setItems(res.data);
    } catch { toast.error('Erreur'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  const handleSave = async (data) => {
    try {
      if (modal === 'edit') {
        await profilService.update(selected.id, data);
        toast.success('Profil mis à jour');
      } else {
        await profilService.create(data);
        toast.success('Profil créé');
      }
      setModal(null); load();
    } catch (err) { toast.error(err.response?.data?.message || 'Erreur'); }
  };

  const handleDelete = async () => {
    try {
      await profilService.delete(deleteId);
      toast.success('Profil supprimé');
      load();
    } catch { toast.error('Erreur de suppression'); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Profils</h1>
          <p className="page-subtitle">Gestion des rôles utilisateurs</p>
        </div>
        {hasRole('ADMINISTRATEUR') && (
          <button className="btn btn-primary" onClick={() => { setSelected(null); setModal('create'); }}>
            <Plus size={16} /> Nouveau profil
          </button>
        )}
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : items.length === 0 ? (
          <div className="empty-state"><Shield size={40} /><p>Aucun profil</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead><tr><th>ID</th><th>Code</th><th>Libellé</th><th>Actions</th></tr></thead>
              <tbody>
                {items.map(p => (
                  <tr key={p.id}>
                    <td style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>#{p.id}</td>
                    <td>{p.code}</td>
                    <td>
                      <span className={`badge ${ROLE_COLORS[p.libelle] || 'badge-gray'}`}>
                        {p.libelle}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        {hasRole('ADMINISTRATEUR') && (
                          <>
                            <button className="btn btn-secondary btn-icon btn-sm"
                              onClick={() => { setSelected(p); setModal('edit'); }}>
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-icon btn-sm"
                              onClick={() => setDeleteId(p.id)}>
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
        title={modal === 'edit' ? 'Modifier le profil' : 'Nouveau profil'}>
        <ProfilForm initial={selected} onSave={handleSave} onClose={() => setModal(null)} />
      </Modal>

      <ConfirmModal open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete}
        message="Supprimer ce profil ?" />
    </div>
  );
}
