import React from 'react'

function sideBarComponent() {
  return (
    <div className="sidebar">
    <div className="sidebar-top">
        <img src={require('../assets/images/logo.png')}/>
        <h1>Life Insurance</h1>
    </div>

    <nav>
      <ul>
        <li className='navButton'>
            <img src={require('../assets/images/dash.png')} className='icon'/>
            <a href="#">Dashboard</a>
        </li>
        <li className='navButton'>
            <img src={require('../assets/images/tax.png')} className='icon'/>
            <a href="#">Tax Management</a>
        </li>
        <li className='navButton'>
            <img src={require('../assets/images/exchange.png')} className='icon'/>
            <a href="#">Stock Exchange</a>
        </li>
      </ul>
    </nav>
  </div>
  )
}

export default sideBarComponent