// app/api/users/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET() {
    const users = await prisma.user.findMany();
    return NextResponse.json(users);
}

export async function POST(request: Request) {
    const body = await request.json();
    const { email, password, fullName, phoneNumber } = body;
    try {
        const existingUser = await prisma.user.findUnique({
            where: { email },
        });

        // TODO: Make these messages for errors
        if (existingUser) {
            return NextResponse.json(
                { error: "Email Already Exists" },
                { status: 500 }
            );
        }

        const user = await prisma.user.create({
            data: { email, password, fullName, phoneNumber },
        });
        return NextResponse.json(user, { status: 201 });
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to create user" },
            { status: 500 }
        );
    }
}
