import { BrowserRouter, Route, Routes } from 'react-router-dom';
import CreateRoom from './views/CreateRoom.jsx';
import Home from './views/Home.jsx';
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Home />} />
        <Route path="/createRoom" element={<CreateRoom />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
