import api from './api';

// ─── AUTH ─────────────────────────────────────────────
export const authService = {
  login: (data)           => api.post('/auth/login', data),
  me: ()                  => api.get('/auth/me'),
  changePassword: (data)  => api.post('/auth/change-password', data),
  forgotPassword: (login, email) =>
    api.post(`/auth/forgot-password?login=${login}&email=${email}`),
};

// ─── ORGANISMES ──────────────────────────────────────
export const organismeService = {
  getAll:   ()          => api.get('/organismes'),
  getById:  (id)        => api.get(`/organismes/${id}`),
  create:   (data)      => api.post('/organismes', data),
  update:   (id, data)  => api.put(`/organismes/${id}`, data),
  delete:   (id)        => api.delete(`/organismes/${id}`),
  search:   (params)    => api.get('/organismes/search', { params }),
};

// ─── EMPLOYES ────────────────────────────────────────
export const employeService = {
  getAll:        (nom)         => api.get('/employes', { params: { nom } }),
  getById:       (id)          => api.get(`/employes/${id}`),
  create:        (data)        => api.post('/employes', data),
  update:        (id, data)    => api.put(`/employes/${id}`, data),
  delete:        (id)          => api.delete(`/employes/${id}`),
  disponibles:   (dateDebut, dateFin) =>
    api.get('/employes/disponibles', { params: { dateDebut, dateFin } }),
  getPhases:     (id)          => api.get(`/employes/${id}/phases`),
};

// ─── PROFILS ─────────────────────────────────────────
export const profilService = {
  getAll:   ()         => api.get('/profils'),
  getById:  (id)       => api.get(`/profils/${id}`),
  create:   (data)     => api.post('/profils', data),
  update:   (id, data) => api.put(`/profils/${id}`, data),
  delete:   (id)       => api.delete(`/profils/${id}`),
};

// ─── PROJETS ─────────────────────────────────────────
export const projetService = {
  getAll:       ()            => api.get('/projets'),
  getById:      (id)          => api.get(`/projets/${id}`),
  create:       (data)        => api.post('/projets', data),
  update:       (id, data)    => api.put(`/projets/${id}`, data),
  delete:       (id)          => api.delete(`/projets/${id}`),
  getByCode:    (code)        => api.get(`/projets/code/${code}`),
  updateMontant:(id, montant) => api.patch(`/projets/${id}/montant`, null, { params: { montant } }),
  affecterChef: (id, chefId)  => api.patch(`/projets/${id}/affecter-chef`, null, { params: { chefId } }),
  getSummary:   (id)          => api.get(`/projets/${id}/resume`),
  getPhases:    (id)          => api.get(`/projets/${id}/phases`),
};

// ─── PHASES ──────────────────────────────────────────
export const phaseService = {
  getByProjet:    (projetId)  => api.get(`/projets/${projetId}/phases`),
  getById:        (id)        => api.get(`/phases/${id}`),
  create:         (data)      => api.post('/phases', data),
  update:         (id, data)  => api.put(`/phases/${id}`, data),
  delete:         (id)        => api.delete(`/phases/${id}`),
  setRealisation: (id)        => api.patch(`/phases/${id}/realisation`),
  setFacturation: (id)        => api.patch(`/phases/${id}/facturation`),
  setPaiement:    (id)        => api.patch(`/phases/${id}/paiement`),
  termineesNonFacturees: ()   => api.get('/phases/terminees-non-facturees'),
  factureesNonPayees:   ()    => api.get('/phases/facturees-non-payees'),
};

// ─── AFFECTATIONS ────────────────────────────────────
export const affectationService = {
  getByPhase:     (phaseId)              => api.get(`/phases/${phaseId}/employes`),
  getOne:         (phaseId, employeId)   => api.get(`/phases/${phaseId}/employes/${employeId}`),
  create:         (phaseId, employeId, data) =>
    api.post(`/phases/${phaseId}/employes/${employeId}`, data),
  update:         (phaseId, employeId, data) =>
    api.put(`/phases/${phaseId}/employes/${employeId}`, data),
  delete:         (phaseId, employeId)   => api.delete(`/phases/${phaseId}/employes/${employeId}`),
  getByEmploye:   (employeId)            => api.get(`/employes/${employeId}/phases`),
};

// ─── LIVRABLES ───────────────────────────────────────
export const livrableService = {
  getByPhase: (phaseId)       => api.get(`/phases/${phaseId}/livrables`),
  getById:    (id)            => api.get(`/livrables/${id}`),
  create:     (phaseId, data) => api.post(`/phases/${phaseId}/livrables`, data),
  update:     (id, data)      => api.put(`/livrables/${id}`, data),
  delete:     (id)            => api.delete(`/livrables/${id}`),
};

// ─── DOCUMENTS ───────────────────────────────────────
export const documentService = {
  getByProjet:  (projetId)       => api.get(`/projets/${projetId}/documents`),
  getById:      (id)             => api.get(`/documents/${id}`),
  create:       (projetId, data) => api.post(`/projets/${projetId}/documents`, data),
  update:       (id, data)       => api.put(`/documents/${id}`, data),
  delete:       (id)             => api.delete(`/documents/${id}`),
  search:       (libelle)        => api.get('/documents/search', { params: { libelle } }),
  downloadUrl:  (id)             => `http://localhost:8080/api/documents/${id}/download`,
};

// ─── FACTURES ────────────────────────────────────────
export const factureService = {
  getAll:       ()            => api.get('/factures'),
  getById:      (id)          => api.get(`/factures/${id}`),
  create:       (phaseId, data) => api.post(`/phases/${phaseId}/facture`, data),
  update:       (id, data)    => api.put(`/factures/${id}`, data),
  delete:       (id)          => api.delete(`/factures/${id}`),
  getByDate:    (date)        => api.get('/factures/byDate', { params: { date } }),
  getByCode:    (code)        => api.get('/factures/byCode', { params: { code } }),
};

// ─── REPORTING ───────────────────────────────────────
export const reportingService = {
  termineesNonFacturees: () => api.get('/phases/terminees-non-facturees'),
  factureesNonPayees:    () => api.get('/phases/facturees-non-payees'),
  allProjets:            () => api.get('/projets'),
  allFactures:           () => api.get('/factures'),
};
