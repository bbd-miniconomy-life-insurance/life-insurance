const BASE = 'https://api.frontend.life.projects.bbdgrad.com';
export const makeGetRequest = async (path,globalState, setGlobalState) => {
    try {
        const response = await fetch(`${BASE}/api/v1/${path}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${globalState.jwtToken}`
            }
        });

        if (!response.ok) {
            if (response.status === 401) {
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
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
};

export const getPaginatedData = async (path,globalState, setGlobalState) => {
    try {
        const params = new URLSearchParams({ page:globalState.page, size:8 });

        const response = await fetch(`${BASE}/api/v1/${path}?${params.toString()}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${globalState.jwtToken}`
            }
        });

        if (!response.ok) {
            if (response.status === 401) {
                setGlobalState({
                    ...globalState,
                    isLoggedIn: false,
                    jwtToken: null,
                    accessToken: null
                });

                localStorage.removeItem('id_token');
                localStorage.removeItem('access_token');
                window.location.pathname = '';
            }
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
};



