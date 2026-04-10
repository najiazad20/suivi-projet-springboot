import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import { PrivateRoute, RoleRoute } from './guards/RouteGuards';

// Layout
import AppLayout from './components/layout/AppLayout';


// Auth pages (Vérifiez que ProfilPage est bien dans auth et non dans profils)
import LoginPage   from './pages/auth/LoginPage';
import ProfilPage  from './pages/auth/ProfilPage'; 

// Main pages
// Note : Si DashboardPage est à la racine de /pages, gardez ce chemin
import DashboardPage     from './pages/reporting/DashboardPage'; 

// Pages dans des sous-dossiers (Vérifiez bien l'extension .jsx ou .js)
import OrganismesPage    from './pages/organismes/OrganismesPage';
import EmployesPage      from './pages/employes/EmployesPage';
import ProfilsPage       from './pages/profils/ProfilsPage';
import ProjetsPage       from './pages/projets/ProjetsPage';
import ProjetDetailPage  from './pages/projets/ProjetDetailPage';
import PhasesPage        from './pages/phases/PhasesPage';
import PhaseDetailPage   from './pages/phases/PhaseDetailPage'; // L'erreur venait souvent d'ici
import AffectationsPage  from './pages/affectations/AffectationsPage';
import LivrablesPage     from './pages/livrables/LivrablesPage';
import DocumentsPage     from './pages/documents/DocumentsPage';
import FacturesPage      from './pages/factures/FacturesPage';   // L'erreur venait aussi d'ici
import ReportingPage     from './pages/reporting/ReportingPage';

// Pages d'erreurs
// Note : Si vous n'avez pas de fichier ErrorPages.jsx, créez-le ou vérifiez le nom
import { Page403, Page404 } from './pages/reporting/ErrorPages';

const ALL_ROLES = ['ADMINISTRATEUR','DIRECTEUR','SECRETAIRE','CHEF_PROJET','COMPTABLE'];

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Toaster
          position="top-right"
          toastOptions={{
            style: {
              background: 'var(--bg-card)',
              color: 'var(--text-primary)',
              border: '1px solid var(--border)',
              fontFamily: 'var(--font-body)',
              fontSize: '0.875rem',
            },
            success: { iconTheme: { primary: '#10b981', secondary: '#fff' } },
            error:   { iconTheme: { primary: '#f43f5e', secondary: '#fff' } },
          }}
        />

        <Routes>
          {/* Public */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/403"   element={<Page403 />} />
          <Route path="/404"   element={<Page404 />} />

          {/* Protected layout */}
          <Route element={<PrivateRoute><AppLayout /></PrivateRoute>}>

            {/* Default redirect */}
            <Route index element={<Navigate to="/dashboard" replace />} />

            {/* Dashboard — all roles */}
            <Route path="/dashboard" element={
              <RoleRoute roles={ALL_ROLES}><DashboardPage /></RoleRoute>
            } />

            {/* Mon profil — all roles */}
            <Route path="/profil" element={
              <RoleRoute roles={ALL_ROLES}><ProfilPage /></RoleRoute>
            } />

            {/* Organismes — SECRETAIRE, DIRECTEUR, ADMINISTRATEUR */}
            <Route path="/organismes" element={
              <RoleRoute roles={['SECRETAIRE','DIRECTEUR','ADMINISTRATEUR']}>
                <OrganismesPage />
              </RoleRoute>
            } />

            {/* Employés — ADMINISTRATEUR, DIRECTEUR, CHEF_PROJET */}
            <Route path="/employes" element={
              <RoleRoute roles={['ADMINISTRATEUR','DIRECTEUR','CHEF_PROJET']}>
                <EmployesPage />
              </RoleRoute>
            } />

            {/* Profils — ADMINISTRATEUR only */}
            <Route path="/profils" element={
              <RoleRoute roles={['ADMINISTRATEUR']}>
                <ProfilsPage />
              </RoleRoute>
            } />

            {/* Projets */}
            <Route path="/projets" element={
              <RoleRoute roles={['SECRETAIRE','DIRECTEUR','CHEF_PROJET','ADMINISTRATEUR']}>
                <ProjetsPage />
              </RoleRoute>
            } />
            <Route path="/projets/:id" element={
              <RoleRoute roles={['SECRETAIRE','DIRECTEUR','CHEF_PROJET','ADMINISTRATEUR']}>
                <ProjetDetailPage />
              </RoleRoute>
            } />

            {/* Phases */}
            <Route path="/phases" element={
              <RoleRoute roles={['CHEF_PROJET','DIRECTEUR','ADMINISTRATEUR']}>
                <PhasesPage />
              </RoleRoute>
            } />
            <Route path="/phases/:id" element={
              <RoleRoute roles={['CHEF_PROJET','DIRECTEUR','ADMINISTRATEUR','COMPTABLE']}>
                <PhaseDetailPage />
              </RoleRoute>
            } />

            {/* Affectations */}
            <Route path="/affectations" element={
              <RoleRoute roles={['CHEF_PROJET','ADMINISTRATEUR']}>
                <AffectationsPage />
              </RoleRoute>
            } />

            {/* Livrables */}
            <Route path="/livrables" element={
              <RoleRoute roles={['CHEF_PROJET','DIRECTEUR','ADMINISTRATEUR']}>
                <LivrablesPage />
              </RoleRoute>
            } />

            {/* Documents */}
            <Route path="/documents" element={
              <RoleRoute roles={['CHEF_PROJET','DIRECTEUR','SECRETAIRE','ADMINISTRATEUR']}>
                <DocumentsPage />
              </RoleRoute>
            } />

            {/* Factures — COMPTABLE, ADMINISTRATEUR */}
            <Route path="/factures" element={
              <RoleRoute roles={['COMPTABLE','ADMINISTRATEUR']}>
                <FacturesPage />
              </RoleRoute>
            } />

            {/* Reporting */}
            <Route path="/reporting" element={
              <RoleRoute roles={['DIRECTEUR','CHEF_PROJET','COMPTABLE','ADMINISTRATEUR']}>
                <ReportingPage />
              </RoleRoute>
            } />

            {/* Catch all */}
            <Route path="*" element={<Page404 />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
