import React from 'react'
import { useGlobalState } from '../state/GlobalState'

function HeaderComponent() {
    const { globalState, setGlobalState } = useGlobalState();
    const handleSignOut =()=>{
        setGlobalState({
            ...globalState,
            isLoggedIn: false,
            jwtToken: null,
            accessToken: null,
          });
        
          localStorage.removeItem('id_token');
          localStorage.removeItem('access_token');
          window.location.pathname = '';
    }

    return (
        <div className="header">
            <h2>Hello, user</h2>
            {globalState.isLoggedIn?
                <button onClick={handleSignOut}>Sign out</button>:''
            }
            
        </div>
    )
}

export default HeaderComponent