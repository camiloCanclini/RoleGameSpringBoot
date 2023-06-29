import './Card.css'
import '../../woodPattern.css'

function Card (){

  return (
    <div className='flex flex-col w-32 z-10 mx-8 mt-6 h-56 overflow-hidden'>
      <img src="https://pngimg.com/d/medival_knight_PNG15928.png" alt="" className='z-30'/>
      <img src="https://e1.pxfuel.com/desktop-wallpaper/327/350/desktop-wallpaper-medieval-times-medieval-background.jpg" alt="" className='absolute z-20 top-0 bottom-0 w-full h-full'/>
      <div className='woodPattern'>
        <p>Carta</p>
      </div>
    </div>
  );
  
}

export default Card