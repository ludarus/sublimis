import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './home/Home.tsx'
import Ws from './ws/WebSocket.tsx'

export default function App() {
	return (
		<BrowserRouter>
			<Routes>
				<Route path="/" element={<Home/>}/>
				<Route path="/home" element={<Home/>}/>
				<Route path="/ws" element={<Ws/>}/>
			</Routes>
		</BrowserRouter>
	)

}
