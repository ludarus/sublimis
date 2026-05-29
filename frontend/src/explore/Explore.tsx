import { Suspense, useEffect, useState } from 'react'
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom"
import type { LiveCampaign } from '../types/LiveCampaign'

import './Explore.css'

export default function Expore() {
	const [liveCampaigns, setLiveCampaigns] = useState<LiveCampaign | null>(null)

	const navigate = useNavigate()
	const [clickable, setClickable] = useState(true)

	type CampaignId = {
		cid: String
	}
	async function handleClick() {
		if (clickable) {
			setClickable(false)
			const res = await fetch(
				'http://localhost:9000/createLiveCampaign', {
				method: 'GET',
				credentials: 'include'
			})
			const cidJson: CampaignId = await res.json()
			setClickable(true)
			navigate(`/live/${cidJson.cid}`)
		}
	}
	//fetching info on refresh 
	useEffect(() => {
		const fetchData = async () => {
			const res = await fetch(
				'http://localhost:9000/getLive',
				{
					method: 'GET',
					credentials: 'include'
				}
			)
			setLiveCampaigns(await res.json())
		};

		fetchData();
	}, [])


	return (
		<div id="root-container-expore">
			<h1>active campaigns right now:</h1>
			{
				liveCampaigns ? <div>{liveCampaigns.cids[0] ?

					<ul>
						{liveCampaigns.cids.map((cid) => (
							<li key={cid}>

								<Link to={`../live/${cid}`}> {cid}</Link>

							</li>
						))}
					</ul>

					:

					"there are no active campaigns right now"}</div> : <p>...</p>
			}

			<br />

			<button onClick={handleClick} className='border-2 rounded-2xl'>
				<h1>create new campaign</h1>
			</button>
		</div >
	)
}
