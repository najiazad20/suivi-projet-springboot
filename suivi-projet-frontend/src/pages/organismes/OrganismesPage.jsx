import { useState, useEffect } from 'react';
import { organismeService } from '../../services/services';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Plus, Search, Pencil, Trash2, Building2, Globe, Phone, Mail, User } from 'lucide-react';

const FIELDS = [
  { name: 'code',         label: 'Code',        placeholder: 'ORG-2026-XXX',   required: true },
  { name: 'nom',          label: 'Nom',          placeholder: 'Microsoft France', required: true },
  { name: 'adresse',      label: 'Adresse',      placeholder: '39 Quai du Président...', required: false },
  { name: 'telephone',    label: 'Téléphone',    placeholder: '01 55 69 55 69',  required: false },
  { name: 'nomContact',   label: 'Contact',      placeholder: 'Jean Dupont',     required: false },
  { name: 'emailContact', label: 'Email contact',placeholder: 'j.dupont@ms.com', required: false },
  { name: 'siteWeb',      label: 'Site web',     placeholder: 'https://...',     required: false },
];

function OrganismeForm({ initial, onSave, onClose }) {
  const { register, handleSubmit, formState: { errors } } = useForm({ defaultValues: initial || {} });

  const onSubmit = async (data) => {
    await onSave(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        {FIELDS.map(f => (
          <div key={f.name} className="form-group">
            <label className="form-label">{f.label}</label>
            <input
              className="form-control"
              placeholder={f.placeholder}
              {...register(f.name, f.required ? { required: `${f.label} obligatoire` } : {})}
            />
            {errors[f.name] && <span className="form-error">{errors[f.name].message}</span>}
          </div>
        ))}
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary"><Plus size={14} /> Enregistrer</button>
      </div>
    </form>
  );
}

export default function OrganismesPage() {
  const { hasRole } = useAuth();
  const [items, setItems]       = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [search, setSearch]     = useState('');
  const [modal, setModal]       = useState(null); // null | 'create' | 'edit'
  const [selected, setSelected] = useState(null);
  const [deleteId, setDeleteId] = useState(null);

  const load = async () => {
    try {
      const res = await organismeService.getAll();
      setItems(res.data);
      setFiltered(res.data);
    } catch { toast.error('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    const q = search.toLowerCase();
    setFiltered(items.filter(o =>
      o.nom?.toLowerCase().includes(q) ||
      o.code?.toLowerCase().includes(q) ||
      o.nomContact?.toLowerCase().includes(q)
    ));
  }, [search, items]);

  const handleSave = async (data) => {
    try {
      if (modal === 'edit') {
        await organismeService.update(selected.id, data);
        toast.success('Organisme mis à jour');
      } else {
        await organismeService.create(data);
        toast.success('Organisme créé');
      }
      setModal(null);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erreur');
    }
  };

  const handleDelete = async () => {
    try {
      await organismeService.delete(deleteId);
      toast.success('Organisme supprimé');
      load();
    } catch { toast.error('Erreur lors de la suppression'); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Organismes</h1>
          <p className="page-subtitle">{filtered.length} organisme(s)</p>
        </div>
        {hasRole('SECRETAIRE', 'ADMINISTRATEUR', 'DIRECTEUR') && (
          <button className="btn btn-primary" onClick={() => { setSelected(null); setModal('create'); }}>
            <Plus size={16} /> Nouvel organisme
          </button>
        )}
      </div>

      {/* Search */}
      <div className="card mb-6">
        <div className="search-bar" style={{ maxWidth: 400 }}>
          <Search size={15} color="var(--text-muted)" />
          <input placeholder="Rechercher par nom, code, contact..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>
      </div>

      {/* Table */}
      <div className="card" style={{ padding: 0 }}>
        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <Building2 size={40} />
            <p>Aucun organisme trouvé</p>
          </div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead><tr>
                <th>Code</th><th>Nom</th><th>Contact</th><th>Téléphone</th><th>Email</th><th>Actions</th>
              </tr></thead>
              <tbody>
                {filtered.map(o => (
                  <tr key={o.id}>
                    <td><code style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{o.code}</code></td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{o.nom}</div>
                      {o.siteWeb && <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{o.siteWeb}</div>}
                    </td>
                    <td>{o.nomContact}</td>
                    <td>{o.telephone}</td>
                    <td>{o.emailContact}</td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        {hasRole('SECRETAIRE', 'ADMINISTRATEUR', 'DIRECTEUR') && (
                          <>
                            <button className="btn btn-secondary btn-icon btn-sm" onClick={() => { setSelected(o); setModal('edit'); }} title="Modifier">
                              <Pencil size={13} />
                            </button>
                            <button className="btn btn-danger btn-icon btn-sm" onClick={() => setDeleteId(o.id)} title="Supprimer">
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

      {/* Modal create/edit */}
      <Modal
        open={modal === 'create' || modal === 'edit'}
        onClose={() => setModal(null)}
        title={modal === 'edit' ? 'Modifier l\'organisme' : 'Nouvel organisme'}
        size="lg"
      >
        <OrganismeForm initial={selected} onSave={handleSave} onClose={() => setModal(null)} />
      </Modal>

      {/* Confirm delete */}
      <ConfirmModal
        open={!!deleteId}
        onClose={() => setDeleteId(null)}
        onConfirm={handleDelete}
        message="Supprimer cet organisme ? Cette action est irréversible."
      />
    </div>
  );
}
