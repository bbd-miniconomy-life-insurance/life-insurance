import React from 'react'

function tableComponent({col1,col2,col3}) {
  return (
    <div className='table-row'>
        <div className='table-col'>{col1}</div>
        <div className='table-col table-middle'>{col2}</div>
        <div className='table-col'>{col3}</div>
    </div>
  )
}

export default tableComponent