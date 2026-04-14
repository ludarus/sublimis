import { GoogleOAuthProvider, GoogleLogin, type CredentialResponse } from '@react-oauth/google'
import { useState } from 'react'
import { motion } from 'motion/react'
import type { SublimisUser } from './types.tsx'

function TestPost() {
	async function postData(msg: String) {
		await fetch(
			'http://localhost:9000/testDump',
			{
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(msg),
				credentials: 'include'
			}
		).then(response => console.log(response))
		// .then(data => console.log(data))
		// .catch(error => console.log(error))
	}
	return (
		<button onClick={() => postData("this is a message")}>
			post something
		</button>
	)
}

export type NavBarProps = {
	userInfo: SublimisUser | null,
	setInfo: React.Dispatch<React.SetStateAction<SublimisUser | null>>
}

export default function NavBar({ userInfo, setInfo }: NavBarProps) {
	const [nav1, setNav1] = useState(false)
	const [logClicked, setLogClicked] = useState(false)


	//functions 
	async function googleLogin(credentialReponse: CredentialResponse) {
		console.log("posting to backend")
		const res = await fetch(
			'http://localhost:9000/googleAuth', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(credentialReponse),
			credentials: 'include'
		}
		)
		setInfo(await res.json())

	}

	function TestGet() {
		async function getData() {
			const res = await fetch(
				'http://localhost:9000/getUserInfo',
				{
					method: 'GET',
					credentials: 'include'
				}
			)
			setInfo(await res.json())
		}
		return (
			<button onClick={() => getData()}>
				get something
			</button>
		)
	}
	//jsx
	return (
		<nav
			id='navbar'
		>
			<div
				className='navbar-element'
				// onMouseEnter={() => { setNav1(true) }}
				// onMouseLeave={() => {
				// 	setNav1(false)
				// }}

				onClick={() => {
					setNav1(!nav1)
				}}
			>

				<h1>testing</h1>
				<motion.div
					className="dropdown
						"
					onClick={(e) => { e.stopPropagation() }}

					animate={{
						height: nav1 ? "10em" : "0em",
					}}
					transition={{
						type: "spring", bounce: 0.45, duration: 0.5
					}}
				>

					<div className='dropdown-element'

					>
						{TestPost()}
					</div >
					<div className='dropdown-element' >
						{TestGet()}
					</div >
					<div className='dropdown-element' >
						<a href='ws'>
							websocket testing
						</a>
					</div >
					<div className='dropdown-element' >
						<p>dropdown thing</p>
					</div >
					<div className='dropdown-element' >
						<p>dropdown thing</p>
					</div >

				</motion.div>

			</div>
			<div
				className='navbar-element
				'>
				<h1>{userInfo ? "Welcome, " + userInfo?.name : "sign in"}</h1>
				<img src={userInfo ? userInfo?.img : "null"} />
			</div>
			<div
				className='navbar-element'

				onClick={() => {
					setLogClicked(!logClicked)
				}}
			>
				<GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
					<GoogleLogin
						onSuccess={credentialReponse => {
							//send token to backend to process shit
							console.log(credentialReponse);
							//sending the token to the backend to get verified and logged
							googleLogin(credentialReponse)
						}
						}
					/>
				</GoogleOAuthProvider>
			</div>
		</nav>
	)
}
