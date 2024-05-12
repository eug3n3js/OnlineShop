import React, {useContext, useState} from 'react';
import {Link} from 'react-router-dom';
import '../styles/NavBar.css'


const Navbar = () => {
    const [isClicked, setIsClicked] = useState(false);
    return (
        <div className="navbar">
            <div className="navbar__left">
                <div className="logo">
                    <h1 className="logo__name">OnlineShop</h1>
                </div>
            </div>
            <div onClick={() => {setIsClicked(false)}} className="navbar__buttons">
                <div className="navbar__button">
                    <Link to="/">Home</Link>
                </div>
                <div className="navbar__button">
                    <Link to="/products">Products</Link>
                </div>
                <div className="navbar__button">
                    <Link to="/shops">Shops</Link>
                </div>
                <div className="navbar__button">
                    <Link to="/orders">Orders</Link>
                </div>
            </div>
        </div>
    );
};

export default Navbar;