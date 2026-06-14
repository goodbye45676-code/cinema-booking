// src/components/admin/MovieForm.jsx
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../services/api";

export default function MovieForm() {
    const { id } = useParams(); // موجود فقط في حالة التعديل
    const navigate = useNavigate();
    const isEditMode = !!id;

    const [formData, setFormData] = useState({
        title: "",
        location: "",
        totalSeats: "",
        price: "",
        showTime: "",
        description: ""
    });
    const [image, setImage] = useState(null);
    const [preview, setPreview] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (isEditMode) {
            fetchMovie();
        }
    }, [id]);

    const fetchMovie = async () => {
        const res = await api.get(`/movies/${id}`);
        const movie = res.data;
        setFormData({
            title: movie.title,
            location: movie.location,
            totalSeats: movie.totalSeats,
            price: movie.price,
            showTime: movie.showTime?.slice(0, 16), // format for datetime-local
            description: movie.description
        });
        setPreview(`http://localhost:8080${movie.imageUrl}`);
    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        setImage(file);
        if (file) setPreview(URL.createObjectURL(file));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);

        const data = new FormData();
        data.append("movie", JSON.stringify({
            ...formData,
            totalSeats: parseInt(formData.totalSeats),
            price: parseFloat(formData.price)
        }));
        if (image) data.append("image", image);

        try {
            if (isEditMode) {
                await api.put(`/admin/movies/${id}`, data, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
            } else {
                await api.post(`/admin/movies`, data, {
                    headers: { "Content-Type": "multipart/form-data" }
                });
            }
            navigate("/admin");
        } catch (err) {
            setError(err.response?.data || "حدث خطأ");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="movie-form-container">
            <h2>{isEditMode ? "تعديل الفيلم" : "إضافة فيلم جديد"}</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleSubmit}>
                <label>عنوان الفيلم</label>
                <input name="title" value={formData.title} onChange={handleChange} required />

                <label>المكان / القاعة</label>
                <input name="location" value={formData.location} onChange={handleChange} required />

                <label>عدد المقاعد الكلي</label>
                <input
                    type="number"
                    name="totalSeats"
                    value={formData.totalSeats}
                    onChange={handleChange}
                    required
                    min="1"
                />

                <label>السعر</label>
                <input
                    type="number"
                    name="price"
                    value={formData.price}
                    onChange={handleChange}
                    required
                    step="0.01"
                    min="0"
                />

                <label>وقت العرض</label>
                <input
                    type="datetime-local"
                    name="showTime"
                    value={formData.showTime}
                    onChange={handleChange}
                    required
                />

                <label>الوصف</label>
                <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    rows="4"
                />

                <label>صورة الفيلم</label>
                <input type="file" accept="image/*" onChange={handleImageChange} />
                {preview && <img src={preview} alt="preview" width="150" />}

                <div className="form-actions">
                    <button type="submit" disabled={loading}>
                        {loading ? "جاري الحفظ..." : isEditMode ? "حفظ التعديلات" : "إضافة الفيلم"}
                    </button>
                    <button type="button" onClick={() => navigate("/admin")}>إلغاء</button>
                </div>
            </form>
        </div>
    );
}