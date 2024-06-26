import React, { useState } from 'react';
import './styles.css';

import TableContentContainer from './assets/components/tableContentContainer';
import SideBarComponent from './assets/components/sideBarComponent';

function App() {
  const [selectedTable, setSelectedTable] = useState('Policies');

  return (
    <div className="dashboard">
      <SideBarComponent />
      <MainContent selectedTable={selectedTable} setSelectedTable={setSelectedTable} />
    </div>
  );
}

function Sidebar() {
  return (
    <div className="sidebar">
      <h1>Life Insurance</h1>
      <nav>
        <ul>
          <li className='navButton'><a href="#">Dashboard</a></li>
          <li className='navButton'><a href="#">Tax Management</a></li>
          <li className='navButton'><a href="#">Stock Exchange</a></li>
        </ul>
      </nav>
    </div>
  );
}

function MainContent({ selectedTable, setSelectedTable }) {
  return (
    <div className="main-content">
      <Header />
      <TableButtons selectedTable={selectedTable} setSelectedTable={setSelectedTable} />
      {/*<TableDisplay selectedTable={selectedTable} />*/}
      <TableContentContainer/>
    </div>
  );
}

function Header() {
  return (
    <div className="header">
      <h2>Hello, user</h2>
      <button>Sign out</button>
    </div>
  );
}

function TableButtons({ selectedTable, setSelectedTable }) {
  return (
    <div className="table-buttons">
      <TableButtonsBox
        title="Policies"
        value="700"
        color="#9E092A"
        active={selectedTable === 'Policies'}
        onClick={() => setSelectedTable('Policies')}
      />
      <TableButtonsBox
        title="Paid Out"
        value="230"
        color="#7DD0F3"
        active={selectedTable === 'Paid Out'}
        onClick={() => setSelectedTable('Paid Out')}
      />
      <TableButtonsBox
        title="Payment History"
        value="1000"
        color="#9E092A"
        active={selectedTable === 'Payment History'}
        onClick={() => setSelectedTable('Payment History')}
      />
    </div>
  );
}

function TableButtonsBox({ title, value, color, active, onClick }) {
  return (
    <div
      className={`table-buttons-box ${active ? 'active' : ''}`}
      style={{ borderColor: color }}
      onClick={onClick}
    >
      <p>{title}</p>
      <h3>{value}</h3>
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
