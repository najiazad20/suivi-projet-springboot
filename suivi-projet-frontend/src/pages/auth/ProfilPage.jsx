import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import { authService } from '../../services/services';
import { User, Mail, Phone, Shield, Lock } from 'lucide-react';

export default function ProfilPage() {
  const { user, role } = useAuth();
  const [loading, setLoading] = useState(false);

  const { register, handleSubmit, reset, formState: { errors } } = useForm();

  const onChangePassword = async (data) => {
    if (data.newPassword !== data.confirm) {
      toast.error('Les mots de passe ne correspondent pas');
      return;
    }
    setLoading(true);
    try {
      await authService.changePassword({ oldPassword: data.oldPassword, newPassword: data.newPassword });
      toast.success('Mot de passe mis à jour');
      reset();
    } catch (err) {
      toast.error(err.response?.data || 'Erreur lors du changement');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 700 }}>
      <div className="page-header">
        <div>
          <h1 className="page-title">Mon Profil</h1>
          <p className="page-subtitle">Informations personnelles et sécurité</p>
        </div>
      </div>

      <div className="card mb-6">
        <div style={{ display: 'flex', alignItems: 'center', gap: 20, marginBottom: 24 }}>
          <div style={{
            width: 60, height: 60,
            background: 'linear-gradient(135deg, var(--accent-blue), var(--accent-violet))',
            borderRadius: '50%',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '1.4rem', fontWeight: 700, color: '#fff',
          }}>
            {user?.prenom?.[0]}{user?.nom?.[0]}
          </div>
          <div>
            <div style={{ fontFamily: 'var(--font-display)', fontSize: '1.3rem', fontWeight: 700 }}>
              {user?.prenom} {user?.nom}
            </div>
            <span className="badge badge-blue">{role?.replace(/_/g, ' ')}</span>
          </div>
        </div>

        <div className="form-grid">
          <div className="form-group">
            <label className="form-label"><User size={12} style={{ display: 'inline', marginRight: 4 }} />Matricule</label>
            <div className="form-control" style={{ background: 'var(--bg-hover)', cursor: 'default' }}>{user?.matricule}</div>
          </div>
          <div className="form-group">
            <label className="form-label"><Mail size={12} style={{ display: 'inline', marginRight: 4 }} />Email</label>
            <div className="form-control" style={{ background: 'var(--bg-hover)', cursor: 'default' }}>{user?.email}</div>
          </div>
          <div className="form-group">
            <label className="form-label"><Phone size={12} style={{ display: 'inline', marginRight: 4 }} />Téléphone</label>
            <div className="form-control" style={{ background: 'var(--bg-hover)', cursor: 'default' }}>{user?.telephone}</div>
          </div>
          <div className="form-group">
            <label className="form-label"><Shield size={12} style={{ display: 'inline', marginRight: 4 }} />Profil</label>
            <div className="form-control" style={{ background: 'var(--bg-hover)', cursor: 'default' }}>{user?.profil?.libelle}</div>
          </div>
        </div>
      </div>

      <div className="card">
        <h2 style={{ fontFamily: 'var(--font-display)', fontSize: '1rem', fontWeight: 700, marginBottom: 20, display: 'flex', alignItems: 'center', gap: 8 }}>
          <Lock size={16} /> Changer le mot de passe
        </h2>
        <form onSubmit={handleSubmit(onChangePassword)} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div className="form-group">
            <label className="form-label">Ancien mot de passe</label>
            <input className="form-control" type="password" placeholder="••••••••"
              {...register('oldPassword', { required: 'Requis' })} />
            {errors.oldPassword && <span className="form-error">{errors.oldPassword.message}</span>}
          </div>
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label">Nouveau mot de passe</label>
              <input className="form-control" type="password" placeholder="••••••••"
                {...register('newPassword', { required: 'Requis', minLength: { value: 6, message: '6 caractères min.' } })} />
              {errors.newPassword && <span className="form-error">{errors.newPassword.message}</span>}
            </div>
            <div className="form-group">
              <label className="form-label">Confirmer</label>
              <input className="form-control" type="password" placeholder="••••••••"
                {...register('confirm', { required: 'Requis' })} />
            </div>
          </div>
          <div>
            <button className="btn btn-primary" type="submit" disabled={loading}>
              {loading ? <div className="spinner" style={{ width: 16, height: 16, borderWidth: 2 }} /> : <><Lock size={14} /> Mettre à jour</>}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
