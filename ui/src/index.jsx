import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './Dashboard.jsx'
import { SettingProvider } from './context/SettingContext.jsx'

createRoot(document.getElementById('root')).render(
  <SettingProvider>
    <StrictMode>
      <App />
    </StrictMode>
  </SettingProvider>
)
