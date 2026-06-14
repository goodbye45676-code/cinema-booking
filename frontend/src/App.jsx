// src/App.jsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/common/Navbar";
import ProtectedRoute from "./components/common/ProtectedRoute";

import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import MovieList from "./components/user/MovieList";
import SeatSelection from "./components/user/SeatSelection";
import Ticket from "./components/user/Ticket";
import MyBookings from "./components/user/MyBookings";

import AdminMovieList from "./components/admin/AdminMovieList";
import MovieForm from "./components/admin/MovieForm";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Navbar />
                <Routes>
                    {/* عام */}
                    <Route path="/" element={<MovieList />} />
                    <Route path="/movies/:id" element={<SeatSelection />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />

                    {/* مستخدم مسجل */}
                    <Route path="/ticket" element={
                        <ProtectedRoute requiredRole="USER"><Ticket /></ProtectedRoute>
                    } />
                    <Route path="/my-bookings" element={
                        <ProtectedRoute requiredRole="USER"><MyBookings /></ProtectedRoute>
                    } />

                    {/* أدمن */}
                    <Route path="/admin" element={
                        <ProtectedRoute requiredRole="ADMIN"><AdminMovieList /></ProtectedRoute>
                    } />
                    <Route path="/admin/movies/new" element={
                        <ProtectedRoute requiredRole="ADMIN"><MovieForm /></ProtectedRoute>
                    } />
                    <Route path="/admin/movies/edit/:id" element={
                        <ProtectedRoute requiredRole="ADMIN"><MovieForm /></ProtectedRoute>
                    } />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;