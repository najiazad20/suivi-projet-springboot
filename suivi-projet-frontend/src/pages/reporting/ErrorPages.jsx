import { Link } from 'react-router-dom';
import { ShieldOff, Home } from 'lucide-react';

export function Page403() {
  return (
    <div style={{
      minHeight: '100vh', display: 'flex', flexDirection: 'column',
      alignItems: 'center', justifyContent: 'center', gap: 16,
      background: 'var(--bg-primary)', padding: 20,
    }}>
      <div style={{
        width: 72, height: 72, borderRadius: 20,
        background: 'rgba(244,63,94,0.15)', border: '1px solid rgba(244,63,94,0.3)',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }}>
        <ShieldOff size={36} color="var(--accent-rose)" />
      </div>
      <div style={{ textAlign: 'center' }}>
        <div style={{ fontFamily: 'var(--font-display)', fontSize: '3rem', fontWeight: 800, color: 'var(--accent-rose)' }}>403</div>
        <div style={{ fontFamily: 'var(--font-display)', fontSize: '1.2rem', fontWeight: 700, marginBottom: 8 }}>Accès refusé</div>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Vous n'avez pas les droits nécessaires pour accéder à cette page.</p>
      </div>
      <Link to="/dashboard" className="btn btn-primary"><Home size={15} /> Retour au tableau de bord</Link>
    </div>
  );
}

export function Page404() {
  return (
    <div style={{
      minHeight: '100vh', display: 'flex', flexDirection: 'column',
      alignItems: 'center', justifyContent: 'center', gap: 16,
      background: 'var(--bg-primary)', padding: 20,
    }}>
      <div style={{ textAlign: 'center' }}>
        <div style={{ fontFamily: 'var(--font-display)', fontSize: '5rem', fontWeight: 800, color: 'var(--border-light)', lineHeight: 1 }}>404</div>
        <div style={{ fontFamily: 'var(--font-display)', fontSize: '1.2rem', fontWeight: 700, marginBottom: 8 }}>Page introuvable</div>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Cette page n'existe pas ou a été déplacée.</p>
      </div>
      <Link to="/dashboard" className="btn btn-primary"><Home size={15} /> Retour au tableau de bord</Link>
    </div>
  );
}
