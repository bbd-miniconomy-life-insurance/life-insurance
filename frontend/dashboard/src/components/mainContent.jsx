import React from 'react'
import { useGlobalState } from '../state/GlobalState';
import TableButtons from './tableButtons';
import HeaderComponent from './headerComponent';
import AuthComponent from './authComponent';
import TableContentContainer from './tableContentContainer';

export default function MainContent({ selectedTable, setSelectedTable,authenticated }) {
    const { globalState, setGlobalState } = useGlobalState();
    const handleNextPage=()=>{
        setGlobalState(prevState => ({
            ...prevState,
            page:prevState.page + 1,
        }));
    }
    return (
      <div className="main-content">
        <HeaderComponent/>
        {authenticated?(
          <>
            <TableButtons selectedTable={selectedTable} setSelectedTable={setSelectedTable} />
            <div className='table-title'><h3>{globalState.selectedTable}</h3></div>
            <TableContentContainer/>
            <div className='next-container' >
                {globalState.displayNext?<button className='next-btn' onClick={handleNextPage}>Next page &gt;</button>:''}
            </div>
            
          </>
        )
        :<AuthComponent/>}
        
        
      </div>
    );
}
