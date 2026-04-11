import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import { Briefcase, Eye, EyeOff, Lock, User } from 'lucide-react';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || '/dashboard';

  const [mode, setMode] = useState('login'); // 'login' or 'forgot'
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);

  const { register, handleSubmit, formState: { errors }, reset } = useForm();

  const onLogin = async (data) => {
    setLoading(true);
    try {
      await login(data.username, data.password);
      toast.success('Connexion réussie');
      navigate(from, { replace: true });
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Identifiants incorrects';
      toast.error(msg);
    } finally {
      setLoading(false);
    }
  };

  const onForgot = async (data) => {
    setLoading(true);
    try {
      const { authService } = await import('../../services/services');
      await authService.forgotPassword(data.username, data.email);
      toast.success('Un nouveau mot de passe a été envoyé à ' + data.email);
      setMode('login');
      reset();
    } catch (err) {
      toast.error('Login ou email introuvable');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-bg-decoration" />
      <div className="login-bg-decoration-2" />

      <div className="login-card">
        <div className="login-logo">
          <div className="login-logo-icon">
            <Briefcase size={22} color="#fff" />
          </div>
          <div>
            <div className="login-title">SuiviProjet</div>
            <div className="login-subtitle">Gestion de projets</div>
          </div>
        </div>

        {mode === 'login' ? (
          <form className="login-form" onSubmit={handleSubmit(onLogin)}>
            <div className="form-group">
              <label className="form-label">Login</label>
              <div style={{ position: 'relative' }}>
                <User size={16} style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input
                  className="form-control"
                  style={{ paddingLeft: 38 }}
                  placeholder="Votre login"
                  {...register('username', { required: 'Login obligatoire' })}
                />
              </div>
              {errors.username && <span className="form-error">{errors.username.message}</span>}
            </div>

            <div className="form-group">
              <label className="form-label">Mot de passe</label>
              <div style={{ position: 'relative' }}>
                <Lock size={16} style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                <input
                  className="form-control"
                  style={{ paddingLeft: 38, paddingRight: 38 }}
                  type={showPass ? 'text' : 'password'}
                  placeholder="••••••••"
                  {...register('password', { required: 'Mot de passe obligatoire' })}
                />
                <button
                  type="button"
                  onClick={() => setShowPass(p => !p)}
                  style={{ position: 'absolute', right: 10, top: '50%', transform: 'translateY(-50%)', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)' }}
                >
                  {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
              {errors.password && <span className="form-error">{errors.password.message}</span>}
            </div>

            <button className="btn btn-primary" type="submit" disabled={loading} style={{ marginTop: 8, justifyContent: 'center', padding: '12px' }}>
              {loading ? <div className="spinner" style={{ width: 18, height: 18, borderWidth: 2 }} /> : 'Se connecter'}
            </button>

            <div style={{ textAlign: 'center' }}>
              <button
                type="button"
                className="btn-text"
                style={{ fontSize: '0.8rem', color: 'var(--text-muted)', background: 'none', border: 'none', cursor: 'pointer' }}
                onClick={() => { setMode('forgot'); reset(); }}
              >
                Mot de passe oublié ?
              </button>
            </div>
          </form>
        ) : (
          <form className="login-form" onSubmit={handleSubmit(onForgot)}>
            <div style={{ marginBottom: 20 }}>
              <h3 style={{ margin: '0 0 8px 0', fontSize: '1rem' }}>Récupération</h3>
              <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', margin: 0 }}>
                Saisissez votre login et l'adresse email associée à votre compte.
              </p>
            </div>

            <div className="form-group">
              <label className="form-label">Login</label>
              <input
                className="form-control"
                placeholder="Votre login"
                {...register('username', { required: 'Requis' })}
              />
            </div>

            <div className="form-group">
              <label className="form-label">Email</label>
              <input
                className="form-control"
                type="email"
                placeholder="votre@email.com"
                {...register('email', { required: 'Email requis' })}
              />
            </div>

            <button className="btn btn-primary" type="submit" disabled={loading} style={{ marginTop: 8, justifyContent: 'center', padding: '12px' }}>
              {loading ? <div className="spinner" style={{ width: 18, height: 18, borderWidth: 2 }} /> : 'Envoyer nouveau mot de passe'}
            </button>

            <div style={{ textAlign: 'center' }}>
              <button
                type="button"
                className="btn-text"
                style={{ fontSize: '0.8rem', color: 'var(--text-muted)', background: 'none', border: 'none', cursor: 'pointer' }}
                onClick={() => { setMode('login'); reset(); }}
              >
                Retour à la connexion
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}
