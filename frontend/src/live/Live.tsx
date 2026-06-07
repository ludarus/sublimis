import { useParams } from "react-router";
import { motion } from 'motion/react'
import { useEffect, useState } from "react"

import useWebSocket from '../ws/useWebSocket.tsx'
import "./Live.css"
import type { SublimisUser } from '../types/SublimisUser.tsx'
import FetchData from '../auth/FetchData.tsx'
import NavBar from '../home/Nav.tsx'
import ChatContent from "./ChatContent.tsx";



export default function Live() {
	const [userInfo, setInfo] = useState<SublimisUser | boolean>(true)
	const [chatToggled, setChatToggled] = useState<boolean>(true)
	const [playerCountToggled, setPlayerCountToggled] = useState<boolean>(false)

	let { campaignId } = useParams();

	//sending campaignId in url. should only connect to websocket if user is verified.
	useEffect(() => {
		FetchData(setInfo);
	}, []); // runs once on page load

	const { messages, sendMessage, ready, error, playerlist } = useWebSocket(`ws://localhost:9000/live/${campaignId}`, userInfo !== null)

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

		return (
			<div id="base-container-live" className="border">

				<div id="navbar">
					<NavBar setInfo={setInfo} userInfo={userInfo} />
				</div>
				<div id="root-container-live" className="border">
					<motion.div id="chat-container" className="border-3 border-blue-500" animate={{ width: chatToggled ? "25vw" : "auto" }} transition={{ duration: 0.2, ease: "easeOut" }}>

						<motion.div id="chat-buttons" className="border"
							animate={{ flexDirection: chatToggled ? "row" : "column" }}
						>
							{chatToggled ? (
								//chat enabled
								<button id="players-button"  >
									<svg width="2.307em" height="2em" viewBox="0 0 991 859" fill="none" xmlns="http://www.w3.org/2000/svg" onClick={() => setPlayerCountToggled(!playerCountToggled)}>
										<path d="M78.2488 325.105L78.4974 324.962L159.297 464.911L110.8 492.911L30 352.962L30.2486 352.818L24.2487 287L78.2488 325.105ZM58.1699 350.554L112.57 444.777L116.727 442.377L62.3275 348.154L58.1699 350.554Z" fill="#6C6247" />
										<path d="M960.049 346.818L960.297 346.962L879.497 486.911L831 458.911L911.8 318.962L912.048 319.105L966.049 281L960.049 346.818ZM927.97 342.154L873.57 436.377L877.727 438.777L932.127 344.554L927.97 342.154Z" fill="#6C6247" />
										<path d="M661.311 268.772L661.622 268.952L560.622 443.889L500 408.889L601 233.952L601.311 234.132L668.811 186.5L661.311 268.772ZM621.213 262.942L553.213 380.722L558.409 383.722L626.409 265.942L621.213 262.942Z" fill="#6C6247" />
										<circle cx="500" cy="125" r="125" fill="#1C1103" />
										<circle cx="163" cy="232" r="100" fill="#1C1103" />
										<rect x="325" y="301" width="350" height="558" rx="92" fill="#1C1103" />
										<rect x="23" y="369" width="280" height="440" rx="70" fill="#1C1103" />
										<circle cx="837" cy="232" r="100" fill="#1C1103" />
										<rect x="697" y="369" width="280" height="440" rx="70" fill="#1C1103" />
									</svg>
								</button>
							)
								:
								<div />
							}

							<button id="hide-chat-button" onClick={() => setChatToggled(!chatToggled)} >
								<motion.svg width="1.291em" height="2em" viewBox="0 0 102 158" fill="none" xmlns="http://www.w3.org/2000/svg" animate={{ rotateZ: chatToggled ? 0 : "180deg" }} >
									<g filter="url(#filter0_g_5_9)">
										<path d="M8.10001 78.8107L78.8107 8.09999L92.9528 22.2421L36.3843 78.8107L92.9528 135.379L78.8107 149.521L8.10001 78.8107Z" fill="#1C1103" />
									</g>
									<defs>
										<filter id="filter0_g_5_9" x="5.72205e-06" y="5.72205e-06" width="101.053" height="157.621" filterUnits="userSpaceOnUse" color-interpolation-filters="sRGB">
											<feFlood flood-opacity="0" result="BackgroundImageFix" />
											<feBlend mode="normal" in="SourceGraphic" in2="BackgroundImageFix" result="shape" />
											<feTurbulence type="fractalNoise" baseFrequency="0.25641024112701416 0.25641024112701416" numOctaves="3" seed="8837" />
											<feDisplacementMap in="shape" scale="16.200000762939453" xChannelSelector="R" yChannelSelector="G" result="displacedImage" width="100%" height="100%" />
											<feMerge result="effect1_texture_5_9">
												<feMergeNode in="displacedImage" />
											</feMerge>
										</filter>
									</defs>
								</motion.svg>
							</button>
						</motion.div>

						<ChatContent
							chatToggled={chatToggled}
							playerCountToggled={playerCountToggled}
							messages={messages}
							playerlist={playerlist}
							sendMessage={sendMessage}
						/>

					</motion.div>
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
