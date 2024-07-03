import React, { useState } from 'react';
import './styles.css';

import { useGlobalState } from './state/GlobalState';

import TableContentContainer from './components/tableContentContainer';
import SideBarComponent from './components/sideBarComponent';
import AuthComponent from './components/authComponent';
import HeaderComponent from './components/headerComponent';
import TableButtons from './components/tableButtons';
import MainContent from './components/mainContent';

function App() {
  const [selectedTable, setSelectedTable] = useState('Policies');
  const { globalState, setGlobalState } = useGlobalState();

  return (
    <div className="dashboard">
      <SideBarComponent />
      <MainContent selectedTable={selectedTable} setSelectedTable={setSelectedTable} authenticated={globalState.isLoggedIn}/>
    </div>
  );
}





function TableDisplay({ selectedTable }) {
  let data;
  switch (selectedTable) {
    case 'Policies':
      data = mockData.policies;
      break;
    case 'Paid Out':
      data = mockData.paidOut;
      break;
    case 'Payment History':
      data = mockData.paymentHistory;
      break;
    default:
      data = [];
  }

  return (
    <div className="table-container">
      <h3>{selectedTable}</h3>
      <table className="table">
        <thead>
          <tr>
            <th>Policy</th>
            <th>Date</th>
            <th>Column 3</th>
            <th>Column 4</th>
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => (
            <tr key={index}>
              <td>{row.policy}</td>
              <td>{row.date}</td>
              <td>{row.column3}</td>
              <td>{row.column4}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const mockData = {
  policies: [
    { policy: 'Policy 1', date: '2024-01-01', column3: 'Value 3', column4: 'Value 4' },
    { policy: 'Policy 2', date: '2024-01-02', column3: 'Value 3', column4: 'Value 4' },
    // Add more mock data as needed
  ],
  paidOut: [
    { policy: 'Policy A', date: '2024-02-01', column3: 'Value 3', column4: 'Value 4' },
    { policy: 'Policy B', date: '2024-02-02', column3: 'Value 3', column4: 'Value 4' },
    // Add more mock data as needed
  ],
  paymentHistory: [
    { policy: 'Policy X', date: '2024-03-01', column3: 'Value 3', column4: 'Value 4' },
    { policy: 'Policy Y', date: '2024-03-02', column3: 'Value 3', column4: 'Value 4' },
    // Add more mock data as needed
  ],
};

export default App;
