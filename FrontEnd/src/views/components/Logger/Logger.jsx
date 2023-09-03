import './Logger.css'
import '../../../woodPattern.css'

function Logger ({messagesWs, logs, setLogs, shift, setShift}){
  console.log("messagesws >", messagesWs);
  if (shift) {
    console.log("logsantes >", logs);
    setLogs(logs => [...logs, ...messagesWs])
    console.log("logsdespues >", logs);
    setShift(false)
  }
  return (
    <div className='z-20 border-4 rounded-lg'>
      <div className='woodPattern text-center'> Logs </div>
      <div className='bg-slate-200 h-60 w-96 text-black overflow-y-scroll p-2 text-base'>
        {logs.map((message, index)=><p key={index} className='w-full my-2 break-words'> ▶ {message}</p>)}
      </div>
    </div>
  );
  
}

export default Logger