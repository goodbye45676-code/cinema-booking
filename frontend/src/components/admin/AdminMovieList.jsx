// src/components/admin/AdminMovieList.jsx
import { useEffect, useState } from "react";
import api from "../../services/api";
import { useNavigate } from "react-router-dom";

export default function AdminMovieList() {
    const [movies, setMovies] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetchMovies();
    }, []);

    const fetchMovies = async () => {
        const res = await api.get("/movies");
        setMovies(res.data);
    };

    const handleDelete = async (id) => {
        if (!window.confirm("هل أنت متأكد من حذف هذا الفيلم؟")) return;
        try {
            await api.delete(`/admin/movies/${id}`);
            setMovies(movies.filter((m) => m.id !== id));
        } catch (err) {
            alert("فشل الحذف");
        }
    };

    return (
        <div className="admin-movie-list">
            <div className="admin-header">
                <h2>إدارة الأفلام</h2>
                <button onClick={() => navigate("/admin/movies/new")}>
                    + إضافة فيلم جديد
                </button>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>الصورة</th>
                        <th>العنوان</th>
                        <th>المكان</th>
                        <th>المقاعد</th>
                        <th>السعر</th>
                        <th>الإجراءات</th>
                    </tr>
                </thead>
                <tbody>
                    {movies.map((movie) => (
                        <tr key={movie.id}>
                            <td>
                                <img
                                    src={`http://localhost:8080${movie.imageUrl}`}
                                    alt={movie.title}
                                    width="60"
                                />
                            </td>
                            <td>{movie.title}</td>
                            <td>{movie.location}</td>
                            <td>{movie.availableSeats}/{movie.totalSeats}</td>
                            <td>{movie.price} $</td>
                            <td>
                                <button onClick={() => navigate(`/admin/movies/edit/${movie.id}`)}>
                                    تعديل
                                </button>
                                <button onClick={() => handleDelete(movie.id)} className="delete-btn">
                                    حذف
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}