import { Suspense, useEffect, useState } from 'react'
import { useNavigate } from "react-router-dom"
import type { LiveCampaign } from '../types/LiveCampaign'

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
				liveCampaigns ? liveCampaigns.cids : <p>...</p>
			}

			<br/>
			<br/>

			<button onClick={handleClick} className='border-2 rounded-2xl'>
				<h1>create new campaign</h1>
			</button>
		</div>
	)
}
