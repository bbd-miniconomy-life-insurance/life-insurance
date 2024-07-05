import React from 'react'
import { useGlobalState } from '../state/GlobalState';
import TableButtons from './tableButtons';
import HeaderComponent from './headerComponent';
import AuthComponent from './authComponent';
import TableContentContainer from './tableContentContainer';

export default function MainContent({ selectedTable, setSelectedTable,authenticated }) {
    const { globalState, setGlobalState } = useGlobalState();

    return (
      <div className="main-content">
        <HeaderComponent/>
        {authenticated?(
          <>
            <TableButtons selectedTable={selectedTable} setSelectedTable={setSelectedTable} />
            <div className='table-title'><h3>{globalState.selectedTable}</h3></div>
            <TableContentContainer/>            
          </>
        )
        :<AuthComponent/>}
      </div>
    );
}
