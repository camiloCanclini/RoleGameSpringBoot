import './Logger.css'
import '../../woodPattern.css'

function Looger ({sendMessage, messagesWs}){
  
  return (
    <div className='absolute z-20 border-4 rounded-lg top-5 right-5'>
      <div className='woodPattern'> Logs </div>
      <div className='bg-slate-200 h-60 w-56 text-black overflow-y-scroll p-2 text-base'>
        {messagesWs.map((message, index)=><p key={index} className='w-full break-words'>{message}</p>)}
        <button onClick={sendMessage}>Mensaje</button>
      </div>
    </div>
  );
  
}

export default Looger