// src/components/auth/Login.jsx
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import cinemaImg from "../../assets/cinema-seats.webp";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const userData = await login(email, password);
            navigate(userData.role === "ADMIN" ? "/admin" : "/");
        } catch (err) {
            setError(err.response?.data || "بيانات الدخول غير صحيحة");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="cinema-login">
            <div
                className="cinema-login__visual"
                style={{ backgroundImage: `url(${cinemaImg})` }}
            >
                <div className="cinema-login__overlay" />

                <div className="cinema-login__brand">
                    <span className="cinema-login__eyebrow">— الصالة الكبرى —</span>
                    <h1 className="cinema-login__wordmark">NIGHT REEL</h1>
                    <p className="cinema-login__tagline">
                        لكل مقعد حكاية، ولكل عرض بداية جديدة
                    </p>
                </div>

                <div className="cinema-login__stamp">
                    <span>ADMIT ONE</span>
                    <span className="cinema-login__stamp-code">NR-2026-LOGIN</span>
                </div>
            </div>

            <div className="cinema-login__stub" aria-hidden="true">
                <span className="cinema-login__stub-text">
                    ADMIT ONE • NIGHT REEL • ADMIT ONE
                </span>
            </div>

            <div className="cinema-login__panel">
                <div className="cinema-login__form-wrap">
                    <span className="cinema-login__eyebrow cinema-login__eyebrow--dark">
                        تسجيل الدخول
                    </span>
                    <h2 className="cinema-login__title">مرحباً بعودتك</h2>
                    <p className="cinema-login__subtitle">
                        سجّل دخولك لحجز مقعدك في العرض القادم
                    </p>

                    {error && <p className="cinema-login__error">{error}</p>}

                    <form onSubmit={handleSubmit} className="cinema-login__form">
                        <label className="cinema-login__field">
                            <span>البريد الإلكتروني</span>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="name@example.com"
                                required
                            />
                        </label>

                        <label className="cinema-login__field">
                            <span>كلمة المرور</span>
                            <div className="cinema-login__password">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    placeholder="••••••••"
                                    required
                                />
                                <button
                                    type="button"
                                    className="cinema-login__toggle"
                                    onClick={() => setShowPassword((s) => !s)}
                                >
                                    {showPassword ? "إخفاء" : "إظهار"}
                                </button>
                            </div>
                        </label>

                        <button type="submit" className="cinema-login__submit" disabled={loading}>
                            {loading ? "جاري الدخول..." : "دخول الصالة"}
                        </button>
                    </form>

                    <p className="cinema-login__footer">
                        ليس لديك حساب؟ <Link to="/register">احجز عضويتك الآن</Link>
                    </p>
                </div>
            </div>
        </div>
    );
}