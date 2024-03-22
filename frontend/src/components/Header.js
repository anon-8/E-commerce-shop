import * as React from "react";
// eslint-disable-next-line import/no-anonymous-default-export
export default function (props) {

    return (
        <header className="App-header">
            <img src={props.logoSrc} className="App-logo" alt="logo"/>
            <h1 className="App-title">{props.pageTitle}</h1>
        </header>
    );
};