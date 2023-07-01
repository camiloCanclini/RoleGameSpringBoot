import './Logger.css'
import '../../woodPattern.css'

function Looger (){
  
  return (
    <div className='absolute z-20 border-4 rounded-lg top-5 right-5'>
      <div className='woodPattern'> Logs </div>
      <div className='bg-slate-200 h-60 w-56'>
        <p></p>
      </div>
    </div>
  );
  
}

export default Looger