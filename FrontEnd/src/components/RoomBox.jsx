import './RoomBox.css'

// eslint-disable-next-line react/prop-types
function RoomBox({background, playerHost, roomId}){
  return (
  <a href={'room/'+roomId+'/join'} className="room relative w-1/2 h-32 my-4 flex flex-col justify-center border-l-white border-solid border-4 rounded-full overflow-hidden">
    <img src={background} alt="" className="absolute brightness-75"/>
    <span className="">RoomId: {roomId}</span>
    <span>Player: {playerHost}</span>
  </a>
  );
  
}
export default RoomBox