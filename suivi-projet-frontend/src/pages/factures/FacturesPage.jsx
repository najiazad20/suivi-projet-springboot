import { useState, useEffect } from 'react';
import { factureService, phaseService, projetService } from '../../services/services';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';
import Modal, { ConfirmModal } from '../../components/common/Modal';
import { Plus, Search, Pencil, Trash2, Receipt, Filter } from 'lucide-react';

// Form to create a facture for a phase
function FactureForm({ phases, onSave, onClose, initial, editMode }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    defaultValues: initial ? {
      code: initial.code,
      dateFacture: initial.dateFacture?.substring(0, 10),
      phaseId: initial.phase?.id,
    } : {}
  });

  return (
    <form onSubmit={handleSubmit(onSave)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div className="form-grid">
        <div className="form-group">
          <label className="form-label">Code facture</label>
          <input className="form-control" placeholder="FACT-2026-0001"
            {...register('code', { required: 'Requis' })} />
          {errors.code && <span className="form-error">{errors.code.message}</span>}
        </div>
        <div className="form-group">
          <label className="form-label">Date facture</label>
          <input className="form-control" type="date"
            {...register('dateFacture', { required: 'Requis' })} />
          {errors.dateFacture && <span className="form-error">{errors.dateFacture.message}</span>}
        </div>
        {!editMode && (
          <div className="form-group" style={{ gridColumn: 'span 2' }}>
            <label className="form-label">Phase (doit être réalisée et non facturée)</label>
            <select className="form-control" {...register('phaseId', { required: 'Requis', valueAsNumber: true })}>
              <option value="">-- Sélectionner une phase --</option>
              {phases.map(p => (
                <option key={p.id} value={p.id}>
                  {p.libelle} — {p.montant?.toLocaleString('fr-FR')} MAD
                </option>
              ))}
            </select>
            {errors.phaseId && <span className="form-error">{errors.phaseId.message}</span>}
          </div>
        )}
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onClose}>Annuler</button>
        <button type="submit" className="btn btn-primary"><Receipt size={14} /> Enregistrer</button>
      </div>
    </form>
  );
}

export default function FacturesPage() {
  const { hasRole } = useAuth();
  const [factures, setFactures]     = useState([]);
  const [filtered, setFiltered]     = useState([]);
  const [phases, setPhases]         = useState([]); // eligible phases (terminées non facturées)
  const [loading, setLoading]       = useState(true);
  const [search, setSearch]         = useState('');
  const [filterDate, setFilterDate] = useState('');
  const [modal, setModal]           = useState(null);
  const [selected, setSelected]     = useState(null);
  const [deleteId, setDeleteId]     = useState(null);

  const load = async () => {
    try {
      // load factures
      const facRes = await factureService.getAll();
      setFactures(facRes.data);
      setFiltered(facRes.data);

      // load eligible phases: réalisées non facturées
      const phRes = await phaseService.termineesNonFacturees();
      setPhases(phRes.data);
    } catch (err) {
      toast.error('Erreur de chargement');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  useEffect(() => {
    let result = factures;
    if (search) {
      const q = search.toLowerCase();
      result = result.filter(f =>
        f.code?.toLowerCase().includes(q) ||
        f.phase?.libelle?.toLowerCase().includes(q)
      );
    }
    if (filterDate) {
      result = result.filter(f => f.dateFacture?.startsWith(filterDate));
    }
    setFiltered(result);
  }, [search, filterDate, factures]);

  const handleSave = async (data) => {
    try {
      if (modal === 'edit') {
        await factureService.update(selected.id, { code: data.code, dateFacture: data.dateFacture });
        toast.success('Facture mise à jour');
      } else {
        await factureService.create(data.phaseId, { code: data.code, dateFacture: data.dateFacture });
        toast.success('Facture créée');
      }
      setModal(null);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erreur');
    }
  };

  const handleDelete = async () => {
    try {
      await factureService.delete(deleteId);
      toast.success('Facture supprimée');
      load();
    } catch { toast.error('Erreur de suppression'); }
  };

  // Stats
  const totalFacture = factures.reduce((s, f) => s + (f.phase?.montant || 0), 0);
  const payees = factures.filter(f => f.phase?.etatPaiement).length;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Factures</h1>
          <p className="page-subtitle">{filtered.length} facture(s)</p>
        </div>
        {hasRole('COMPTABLE', 'ADMINISTRATEUR') && (
          <button className="btn btn-primary" onClick={() => { setSelected(null); setModal('create'); }}>
            <Plus size={16} /> Nouvelle facture
          </button>
        )}
      </div>

      {/* Stats */}
      <div className="grid-3 mb-6">
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(59,130,246,0.15)' }}>
            <Receipt size={20} color="var(--accent-blue)" />
          </div>
          <div>
            <div className="stat-label">Total factures</div>
            <div className="stat-value">{factures.length}</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(16,185,129,0.15)' }}>
            <Receipt size={20} color="var(--accent-emerald)" />
          </div>
          <div>
            <div className="stat-label">Payées</div>
            <div className="stat-value">{payees}</div>
            <div className="text-muted text-xs">sur {factures.length}</div>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon" style={{ background: 'rgba(245,158,11,0.15)' }}>
            <Receipt size={20} color="var(--accent-amber)" />
          </div>
          <div>
            <div className="stat-label">Montant total facturé</div>
            <div className="stat-value" style={{ fontSize: '1.1rem' }}>
              {(totalFacture / 1000).toFixed(0)}k MAD
            </div>
          </div>
        </div>
      </div>

      {/* Filters */}
      <div className="card mb-6">
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'center' }}>
          <div className="search-bar" style={{ flex: 1, minWidth: 200 }}>
            <Search size={15} color="var(--text-muted)" />
            <input
              placeholder="Code, phase..."
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <Filter size={14} color="var(--text-muted)" />
            <input
              className="form-control"
              type="month"
              style={{ width: 'auto' }}
              value={filterDate}
              onChange={e => setFilterDate(e.target.value)}
              placeholder="Filtrer par mois"
            />
          </div>
        </div>
      </div>

      {/* Table */}
      <div className="card" style={{ padding: 0 }}>
        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state"><Receipt size={40} /><p>Aucune facture</p></div>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Code</th>
                  <th>Date</th>
                  <th>Phase facturée</th>
                  <th>Montant phase</th>
                  <th>Réalisée</th>
                  <th>Payée</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(f => (
                  <tr key={f.id}>
                    <td>
                      <code style={{ fontSize: '0.78rem', fontWeight: 600, color: 'var(--accent-blue-light)' }}>
                        {f.code}
                      </code>
                    </td>
                    <td style={{ fontSize: '0.85rem' }}>{f.dateFacture}</td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{f.phase?.libelle}</div>
                      <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>
                        {f.phase?.code}
                      </div>
                    </td>
                    <td style={{ fontWeight: 600 }}>
                      {f.phase?.montant?.toLocaleString('fr-FR')} MAD
                    </td>
                    <td>
                      <span className={`badge ${f.phase?.etatRealisation ? 'badge-green' : 'badge-gray'}`}>
                        {f.phase?.etatRealisation ? '✓ Oui' : 'Non'}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${f.phase?.etatPaiement ? 'badge-amber' : 'badge-gray'}`}>
                        {f.phase?.etatPaiement ? '✓ Payée' : 'En attente'}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: 6 }}>
                        {hasRole('COMPTABLE', 'ADMINISTRATEUR') && (
                          <>
                            <button
                              className="btn btn-secondary btn-icon btn-sm"
                              onClick={() => { setSelected(f); setModal('edit'); }}
                              title="Modifier"
                            >
                              <Pencil size={13} />
                            </button>
                            <button
                              className="btn btn-danger btn-icon btn-sm"
                              onClick={() => setDeleteId(f.id)}
                              title="Supprimer"
                            >
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

      {/* Modal */}
      <Modal
        open={modal === 'create' || modal === 'edit'}
        onClose={() => setModal(null)}
        title={modal === 'edit' ? 'Modifier la facture' : 'Nouvelle facture'}
      >
        <FactureForm
          phases={phases}
          onSave={handleSave}
          onClose={() => setModal(null)}
          initial={selected}
          editMode={modal === 'edit'}
        />
      </Modal>

      <ConfirmModal
        open={!!deleteId}
        onClose={() => setDeleteId(null)}
        onConfirm={handleDelete}
        message="Supprimer cette facture ? La phase sera marquée non facturée."
      />
    </div>
  );
}
