import React from 'react'

function TableComponent({col1,col2,col3,col4}) {
  return (
    <div className='table-row'>
        <div className='table-col'>{col1}</div>
        <div className='table-col '>{col2}</div>
        <div className='table-col '>{col3}</div>
        <div className='table-col'>{col4}</div>
    </div>
  )
}

export default TableComponent