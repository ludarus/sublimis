import { Link } from "react-router-dom";
import { GoogleOAuthProvider, GoogleLogin, type CredentialResponse } from '@react-oauth/google'
import Scroll, { ScrollElement } from '../cool/Scroll.tsx'
import type { SublimisUser } from '../types/SublimisUser.tsx'

import "./Nav.css"



export type NavBarProps = {
	userInfo: SublimisUser | null,
	setInfo: React.Dispatch<React.SetStateAction<SublimisUser | null>>
}

export default function NavBar({ userInfo, setInfo }: NavBarProps) {
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

	async function googleLogout() {
		console.log("fetching logout form backend")
		await fetch(
			'http://localhost:9000/logout', {
			method: 'GET',
			credentials: 'include'
		}
		)
		setInfo(null)
	}


	//jsx
	return (
		<nav
			id='navbar'
		>

			<div
				className='navbar-element'
			>
				{userInfo ?
					//signed in
					<div>

				{
						// <div className="navbar-element">
						// 	<Link id="explore-button" className="quicklink" to={`../explore`}>Explore</Link>
						// </div>
				}

						<div className="navbar-element">
							<Scroll title={userInfo.name} pic={userInfo.img} >
								<ScrollElement>
									settings
								</ScrollElement>
								<ScrollElement>
									other stuff
								</ScrollElement>
								<ScrollElement>
									thingies
								</ScrollElement>
								<ScrollElement>
									<p onClick={googleLogout}>logout</p>
								</ScrollElement>
							</Scroll>
						</div>
					</div>
					:
					//not signed in
					<GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
						<GoogleLogin
							onSuccess={credentialReponse => {
								//send token to backend to process shit
								console.log(credentialReponse);
								//sending the token to the backend to get verified and logged
								googleLogin(credentialReponse)
							}}

							theme='outline'
							size='large'
							text='signup_with'
							shape='pill'
							type='standard'
							logo_alignment='left'
							useOneTap={false}
							cancel_on_tap_outside={false}
							auto_select={false}
							ux_mode='popup'

						/>
					</GoogleOAuthProvider>
				}
			</div>

		</nav>
	)
}
