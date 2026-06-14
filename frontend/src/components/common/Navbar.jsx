// src/components/common/Navbar.jsx
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function Navbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <nav className="navbar">
            <Link to="/" className="logo">🎬 BenghaziCinema </Link>
            <div className="nav-links">
                {!user && <Link to="/login">تسجيل الدخول</Link>}
                {user && user.role === "USER" && (
                    <>
                        <Link to="/">الأفلام</Link>
                        <Link to="/my-bookings">حجوزاتي</Link>
                    </>
                )}
                {user && user.role === "ADMIN" && (
                    <Link to="/admin">لوحة التحكم</Link>
                )}
                {user && (
                    <>
                        <span>👤 {user.name}</span>
                        <button onClick={handleLogout}>تسجيل خروج</button>
                    </>
                )}
            </div>
        </nav>
    );
}