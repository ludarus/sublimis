import { useParams } from "react-router";
import { useEffect, useState } from "react"

import useWebSocket from '../ws/useWebSocket.tsx'
import "./Live.css"
import type { SublimisUser } from '../types/SublimisUser.tsx'
import FetchData from '../auth/FetchData.tsx'
import NavBar from '../home/Nav.tsx'



export default function Live() {
	const [userInfo, setInfo] = useState<SublimisUser | boolean>(true)

	let { campaignId } = useParams();

	//sending campaignId in url. should only connect to websocket if user is verified.
	useEffect(() => {
		FetchData(setInfo);
	}, []); // runs once on page load

	const { messages, sendMessage, ready, error } = useWebSocket(`ws://localhost:9000/live/${campaignId}`, userInfo !== null)

	if (!ready) {
		console.log("returning loading")
		return (
			<p>Loading...</p>
		)
	} else {
		if (userInfo === false) {
			console.log("returning no loge")
			return (
				<div id="base-container-live">
					<NavBar setInfo={setInfo} userInfo={userInfo} />
					please log in to access this campaign
				</div>
			)
		}
		if (error !== null) {
			console.log("returning error")
			return (
				<div id="base-container-live">
					<NavBar setInfo={setInfo} userInfo={userInfo} />
					error: {error}
				</div>
			)
		}

		console.log("returning basic")
		return (
			<div id="base-container-live" className="border">

				<div id="navbar">
					<NavBar setInfo={setInfo} userInfo={userInfo} />
				</div>
				<div id="root-container-live" className="border">
					<div id="chat-container" className="border border-blue-500">
						{/*<h1>{campaignId}</h1>
											<p>{wsRef.current ? wsRef.current.readyState : ""}</p>*/}


						<ul id="messages" className="border border-green-500">
							{
								messages.map((msg) => (
									<li className="message " key={Number(msg.time)}>
										<div className="message-image">
											<img className="rounded-full " src={msg.img} />
										</div>
										<div className="message-text">

											<div className="message-headings">
												<h2><b>{msg.name}</b></h2>

												<h3>{new Date(Number(msg.time)).toLocaleTimeString()}</h3>
											</div>
											<p className="message-payload">
												{msg.payload}
											</p>
										</div>
									</li>
								))
							}
						</ul>

						<form action={sendMessage} id="chat-form" >
							<input type="text" className="border-2" id="chat-box" name="chat-msg" />
						</form>
					</div>
					<div id="game-container" className="border">
						<div id="map-container" className="border">
						</div>
						<div id="terminal-container" className="border">
						</div>
					</div>
					<div id="character-container" className="border">
					</div>
				</div >
			</div>
		)
	}
}
