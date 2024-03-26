import React from "react";

const Navbar = (props) => {
    const { isLoggedIn, login, cart, logout, home} = props;

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <div className="container">

                <img src={props.logoSrc} className="App-logo" alt="logo" onClick={home}/>

                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse justify-content-end" id="navbarNav">
                    <ul className="navbar-nav">

                        {isLoggedIn && (
                            <li className="nav-item">
                                <button className="btn btn-outline-info" onClick={cart}>Cart</button>
                            </li>
                        )}
                        <li className="nav-item">
                            {!isLoggedIn ? (
                                <button className="btn btn-outline-primary me-2" onClick={login}>Sign In</button>
                            ) : (
                                <button className="btn btn-outline-danger me-2" onClick={logout}>Logout</button>
                            )}
                        </li>

                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
