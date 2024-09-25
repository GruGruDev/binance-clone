import TreadingHistory from '../Portfilio/TreadingHistory'

const Activity = () => {
  return (
    <div className='px-20'>
      <p className='py-5 pb-10 text-2xl font-semibold'>Recent Transactions</p>
        <TreadingHistory/>
    </div>
  )
}

export default Activity