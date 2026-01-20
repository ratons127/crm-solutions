/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: false,
    eslint: {
        ignoreDuringBuilds: true,
    },
    images: {
        remotePatterns: [
            {
                protocol: "https",
                hostname: "randomuser.me",
                pathname: "/api/portraits/**",
            },
            {
                protocol: "https",
                hostname: "hrm-soltuions.s3.eu-north-1.amazonaws.com",
                pathname: "/**",
            },
        ],
    },
};

export default nextConfig;
