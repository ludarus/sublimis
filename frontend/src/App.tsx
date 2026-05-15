import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './home/Home.tsx'
import Ws from './ws/WebSocket.tsx'
import Live from "./live/Live.tsx";
import Explore from "./explore/Explore.tsx";

export default function App() {
	return (
		<BrowserRouter>
			<Routes>
				<Route path="/" element={<Home />} />
				<Route path="/home" element={<Home />} />
				<Route path="/explore" element={<Explore />} />
				{/*testing*/}
				<Route path="/ws" element={<Ws />} />
				<Route path="/live/:campaignId" element={<Live />} />
			</Routes>
		</BrowserRouter>
	)

}
