import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'image.aladin.co.kr',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8080',
        pathname: '/images/eventBanner/**',
      },
      {
        protocol: 'https',
        hostname:"example.com",
        pathname:"/*" //임시추가
      }
    ],
  },
};

export default nextConfig;
