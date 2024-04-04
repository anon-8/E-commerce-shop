import React from "react";
import WelcomeContent from "./WelcomeContent";
import LoginForm from "../forms/LoginForm";
import { request, setAuthHeader } from "../axios_helper";
import ItemsContent from "./ItemsContent";
import CartComponent from "./CartComponent";
import Navbar from "./Navbar";
import logo from "../logo.svg";

export default class AppContent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            componentToShow: "items",
            isLoggedIn: false
        };
    }

    login = () => {
        this.setState({ componentToShow: "login" });
    };

    logout = () => {
        setAuthHeader(null);
        this.setState({ componentToShow: "items", isLoggedIn: false });
    };

    cart = () => {
        this.setState({ componentToShow: "cart" });
    };

    onLogin = (e, username, password) => {
        e.preventDefault();
        request("POST", "/login", { username, password })
            .then((response) => {
                setAuthHeader(response.data.token);
                this.setState({ componentToShow: "items", isLoggedIn: true });
            })
            .catch((error) => {
                setAuthHeader(null);
                this.setState({ componentToShow: "login" });
            });
    };

    onRegister = (event, firstName, lastName, email, username, password) => {
        event.preventDefault();
        request("POST", "/register", {
            firstName,
            lastName,
            email,
            username,
            password,
        })
            .then((response) => {
                setAuthHeader(response.data.token);
                this.setState({ componentToShow: "items", isLoggedIn: true });
            })
            .catch((error) => {
                setAuthHeader(null);
                this.setState({ componentToShow: "login" });
            });
    };

    home = () => {
        this.setState({ componentToShow: "items" });
    };

    render() {
        const { componentToShow, isLoggedIn } = this.state;

        return (
            <div>
                <Navbar
                    login={this.login}
                    cart={this.cart}
                    logoSrc={logo}
                    isLoggedIn={isLoggedIn}
                    logout={this.logout}
                    home={this.home}
                />

                {componentToShow === "welcome" && <WelcomeContent />}
                {componentToShow === "items" && <ItemsContent />}
                {componentToShow === "cart" && <CartComponent />}
                {componentToShow === "login" && (
                    <LoginForm onLogin={this.onLogin} onRegister={this.onRegister} />
                )}
            </div>
        );
    }
}
