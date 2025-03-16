import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './Dashboard.jsx'
import { SettingProvider } from './contexts/SettingContext.jsx'
import { WebSocketProvider } from './contexts/WebSocketContext.jsx'

createRoot(document.getElementById('root')).render(
  <SettingProvider>
    <WebSocketProvider>
      <StrictMode>
        <App />
      </StrictMode>
    </WebSocketProvider>
  </SettingProvider>
)
