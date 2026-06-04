import { useParams } from "react-router";
import { useEffect, useState } from "react"

import useWebSocket from '../ws/useWebSocket.tsx'
import "./Live.css"
import type { SublimisUser } from '../types/SublimisUser.tsx'
import FetchData from '../auth/FetchData.tsx'
import NavBar from '../home/Nav.tsx'



export default function Live() {
	const [userInfo, setInfo] = useState<SublimisUser | null>(null)

	let { campaignId } = useParams();

	//sending campaignId in url. should only connect to websocket if user is verified.
	useEffect(() => {
		FetchData(setInfo);
	}, []); // runs once on page load

	const { messages, sendMessage, ready, error } = useWebSocket(`ws://localhost:9000/live/${campaignId}`, userInfo !== null)

	if (!ready) {
		return (
			<p>Loading...</p>
		)
	}

	return (
		<div id="base-container-live" className="border">

			<NavBar setInfo={setInfo} userInfo={userInfo} />
			<div id="root-container-live" className="border">
				<div id="chat-container" className="border">

					{userInfo ?
						// user logged in
						(
							<div>
								{error ?
									(<p> error: {error} </p>)
									:
									(<div>

										<h1>{campaignId}</h1>
										{/*	<p>{wsRef.current ? wsRef.current.readyState : ""}</p>*/}
										<form action={sendMessage}>
											<input type="text" className="border-2" id="chat-box" name="chat-msg" />
										</form>

										<ul>
											{
												messages.map((msg) => (
													<li key={Number(msg.time)}>
														[{msg.name}] [{new Date(Number(msg.time)).toLocaleTimeString()}]: {msg.payload}
													</li>
												))
											}
										</ul>

									</div>
									)
								}
							</div>
						)
						:
						(
							//user not logged in
							<div>
								please log in to access this campaign
							</div>
						)
					}
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
