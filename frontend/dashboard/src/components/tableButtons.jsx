import React from 'react'
import { useGlobalState } from '../state/GlobalState';

export default function TableButtons({ selectedTable, setSelectedTable }) {
    const { globalState, setGlobalState } = useGlobalState();
    const handleSelection=(table)=>{
        setGlobalState({
            ...globalState,
            selectedTable:table,
            page:0
          });
    }
    return (
      <div className="table-buttons">
        <TableButtonsBox
          title="Policies"
          value={globalState.policyCount}
          color="#9E092A"
          active={selectedTable === 'Policies'}
          onClick={() => handleSelection('Policies')}
        />
        <TableButtonsBox
          title="Transactions"
          value={globalState.transactionCount}
          color="#7DD0F3"
          active={selectedTable === 'Transactions'}
          onClick={() => handleSelection('Transactions')}
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

