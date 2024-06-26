import React from 'react'
import TableComponent from './tableComponent'
export default function tableContentContainer() {
  let data = generateData();

  return (
    <div className='table-container'>
        <TableComponent
        col1="Policy"
        col2="Date Started"
        col3="PersonaId"/>
        {
            data.map((row,i)=>(
                <TableComponent
                col1={row.policy}
                col2={row.dateStarted}
                col3={row.personaId}/>
            ))
        }
    </div>
  )
}

const generateData = () => {
    const data = [];
    for (let i = 1; i <= 50; i++) {
      data.push({
        policy: `Policy-${i}`,
        dateStarted: new Date(2023, i % 12, i % 28 + 1).toLocaleDateString(),
        personaId: `ID-${Math.floor(Math.random() * 1000)}`,
      });
    }
    return data;
  };
  
