import React from 'react';
import './App.css';
import AppContent from './AppContent';

function App() {
    return (
        <div className="App" style={{position: 'relative', overflow: 'hidden', width: '100vw', height: '100vh'}}>

            <div
                className="absolute inset-x-0 -top-40 -z-10 transform-gpu overflow-hidden blur-3xl sm:-top-80"
                aria-hidden="true"
            >
                <div
                    className="left-[calc(50%-11rem)] aspect-[1155/678] opacity-50 bg-gradient-to-tr from-[#d558c8] to-[#24d292]"
                    aria-hidden="true"
                    style={{
                        clipPath: 'polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)',
                    }}
                />
            </div>

            <div className="relative px-6 lg:px-8" style={{overflowY: 'auto', height: 'calc(100vh)'}}>
                <AppContent/>
            </div>
        </div>
    );
}

export default App;
