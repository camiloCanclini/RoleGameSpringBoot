import './RoomBox.css'

// eslint-disable-next-line react/prop-types
function RoomBox({background, playerHost, roomId}){
  return (
  <a href={'game/'+roomId} className="room relative text-xl w-1/2 h-32 my-4 flex flex-col justify-center border-l-white border-solid border-4 rounded-full overflow-hidden">
    <img src={background} alt="" className="absolute brightness-50"/>
    <div className="mx-10">
      <p>RoomId: {roomId}</p>
      <p>Host Player: {playerHost}</p>
    </div>
  </a>
  );
  
}
export default RoomBox