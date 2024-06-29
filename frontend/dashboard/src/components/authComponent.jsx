import React from 'react'
import { useGlobalState } from '../state/GlobalState';

const AuthComponent = () => {
  const { globalState, setGlobalState } = useGlobalState();


  const handleSignIn = async () => {
    try {
      const cognitoSignInUrl = `https://life-insurance.auth.eu-west-1.amazoncognito.com/login?response_type=token&client_id=3b1g20lnbk4h35i28r54j07qsl&redirect_uri=http://localhost:3000/auth`;


      window.location.href = cognitoSignInUrl;
    } catch (error) {
      console.error('Error signing in:', error);
    }
  };

  React.useEffect(() => {
    const extractTokensFromHash = () => {
      const hash = window.location.hash.substring(1);
      const tokenParams = new URLSearchParams(hash);

      if (tokenParams.has('id_token') && tokenParams.has('access_token')) {
        const idToken = tokenParams.get('id_token');
        const accessToken = tokenParams.get('access_token');

        localStorage.setItem('id_token', idToken);
        localStorage.setItem('access_token', accessToken);

        setGlobalState({
          ...globalState,
          isLoggedIn: true,
          jwtToken: idToken,
          accessToken: accessToken,
        });

        window.location.hash = '';
      }
    };

      const loadTokensFromLocalStorage = () => {
      const idToken = localStorage.getItem('id_token');
      const accessToken = localStorage.getItem('access_token');

      if (idToken && accessToken) {
        setGlobalState({
          ...globalState,
          isLoggedIn: true,
          jwtToken: idToken,
          accessToken: accessToken,
        });
      }
    };

    extractTokensFromHash();
    loadTokensFromLocalStorage();
  }, [setGlobalState, globalState]);


  return (
    <div className="auth-component">
      <h2>You are not logged in...</h2>
      <button onClick={handleSignIn}>Sign In</button>
    </div>
  );
};

export default AuthComponent;
