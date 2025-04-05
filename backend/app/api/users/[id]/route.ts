// app/api/users/[id]/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;

    const user = await prisma.user.findUnique({ where: { id } });
    if (!user) {
        return NextResponse.json({ error: "User not found" }, { status: 404 });
    }
    return NextResponse.json(user);
}

export async function PUT(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;

    const body = await request.json();
    try {
        const updatedUser = await prisma.user.update({
            where: { id },
            data: body, // allow partial updates; validate as needed
        });
        return NextResponse.json(updatedUser);
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to update user" },
            { status: 500 }
        );
    }
}

export async function DELETE(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;
    try {
        await prisma.user.delete({ where: { id } });
        return NextResponse.json({ message: "User deleted" });
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to delete user" },
            { status: 500 }
        );
    }
}
