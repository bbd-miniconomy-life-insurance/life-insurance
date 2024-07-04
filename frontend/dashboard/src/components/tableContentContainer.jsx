import React, { useEffect, useState } from 'react';
import { makeGetRequest,getPaginatedData } from '../service/_api';
import { useGlobalState } from '../state/GlobalState';
import TableComponent from './tableComponent';

export default function TableContentContainer() {
  const { globalState, setGlobalState } = useGlobalState();
  const [tableData, setTableData] = useState([]);

  useEffect(() => {
    const fetchTableData = async () => {
      try {
        const data = await getPaginatedData(globalState.selectedTable.toLowerCase(),globalState, setGlobalState);
        setTableData(data);
        setGlobalState({
          ...globalState,
          displayNext:data.length>=8
        });
      } catch (error) {
        console.error('Error fetching policies:', error);
      }
    };

    const fetchStats = async () => {
      try {
        const data = await makeGetRequest('counts',globalState, setGlobalState);
        setGlobalState({
          ...globalState,
          policyCount:data.policyCount,
          transactionCount: data.transactionCount
        });
      } catch (error) {
        console.error('Error fetching policies:', error);
      }
    };

    if (globalState.isLoggedIn) {
      fetchTableData();
      fetchStats();
    }
  }, [globalState.isLoggedIn, globalState.selectedTable,globalState.page]);


  return (
    <div className='table-container'>
        {globalState.selectedTable === 'Policies' && (
            <div className='table-header'>
                <TableComponent
                    col1="PolicyId"
                    col2="Persona Id"
                    col3="Inception Date"
                    col4="Status"
                />
                <div className='table-content'>
                    {tableData.length > 0 ? (
                        tableData.map((row) => (
                            <TableComponent
                                key={row.id || row.personaId}
                                col1={row.id}
                                col2={row.personaId}
                                col3={row.inceptionDate}
                                col4={row.status}
                            />
                        ))
                    ) : (
                        <h3>No data...</h3>
                    )}
                </div>
            </div>
        )}

        {globalState.selectedTable === 'Transactions' && (
            <div className='table-header'>
                <TableComponent
                    col1="Transaction Id"
                    col2="Date"
                    col3="Reference"
                    col4="Amount (&#208;)"
                />
                <div className='table-content'>
                    {tableData.length > 0 ? (
                        tableData.map((row) => (
                            <TableComponent
                                key={row.id}
                                col1={row.id}
                                col2={row.date}
                                col3={row.reference}
                                col4={row.amount}
                            />
                        ))
                    ) : (
                        <h3>No data...</h3>
                    )}
                </div>
            </div>
        )}
    </div>
);
};
