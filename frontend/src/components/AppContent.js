import React from "react";
import WelcomeContent from "./WelcomeContent";
import LoginForm from "./LoginForm";
import {request, setAuthHeader} from "../axios_helper";
import Buttons from "./Buttons";
import ItemsContent from "./ItemsContent";

export default class AppContent extends  React.Component {

    constructor(props) {
        super(props);
        this.state = {
            componentToShow: "items"
        };
    };

    login = () => {
        this.setState({componentToShow: "login"});
    };
    logout = () => {
        this.setState({componentToShow: "items"});
    };

    onLogin = (e, username, password) => {
        e.preventDefault();
        request(
            "POST",
            "/login",
            {
                username: username,
                password: password
            }).then(
            (response) => {
                setAuthHeader(response.data.token);
                this.setState({componentToShow: "items"});
            }).catch(
            (error) => {
                setAuthHeader(null);
                this.setState({componentToShow: "welcome"})
            }
        );
    };

    onRegister = (event, firstName, lastName, email, username, password) => {
        event.preventDefault();
        request(
            "POST",
            "/register",
            {
                firstName: firstName,
                lastName: lastName,
                email: email,
                username: username,
                password: password
            }).then(
            (response) => {
                setAuthHeader(response.data.token);
                this.setState({componentToShow: "items"});
            }).catch(
            (error) => {
                setAuthHeader(null);
                this.setState({componentToShow: "welcome"})
            }
        );
    };


    render() {
        return (

            <div>

                <Buttons login={this.login} logout={this.logout}/>

                {this.state.componentToShow === "welcome" && <WelcomeContent/>}
                {this.state.componentToShow === "items" && <ItemsContent/>}
                {this.state.componentToShow === "login" && <LoginForm onLogin={this.onLogin} onRegister={this.onRegister}/>}

            </div>
        );
    };


}